package com.example.testCompose.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testCompose.common.LanguageDataStore
import com.example.testCompose.domain.entity.language.LanguageItem
import com.example.testCompose.domain.interactor.useCase.GetLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(private val getLanguageUseCase: GetLanguageUseCase,
    private val languageDataStore: LanguageDataStore) : ViewModel() {

    val selectedLanguage = languageDataStore.language
    val onSettingsChanged = MutableLiveData<Unit>()
    val uiState = MutableStateFlow(UiState())

    var uiValue get() = uiState.value
        set(value) {
            uiState.value = value
        }

    fun fetchLanguages() {
        val canFetchLanguages = uiValue.languages.isEmpty() && uiValue.showLoading.not()
        if (canFetchLanguages) {
            viewModelScope.launch {
                uiValue = uiValue.copy(showLoading = true)
                uiValue = try {
                    val languages = getLanguageUseCase.execute().sortedBy(LanguageItem::english_name)
                    uiValue.copy(showLoading = false, languages = languages)
                } catch (exception: Exception) {
                    uiValue.copy(showLoading = false)
                }
            }
        }
    }

    fun onLanguageSelected(language: LanguageItem) {
        viewModelScope.launch(Dispatchers.IO) {
            languageDataStore.onLanguageSelected(language)
        }
        onSettingsChanged.value = Unit
    }

    data class UiState(
        val showLoading: Boolean = false,
        val languages: List<LanguageItem> = emptyList()
    )
}