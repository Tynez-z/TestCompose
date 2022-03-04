package com.example.testCompose.domain.interactor.useCase

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.language.LanguageItem
import com.example.testCompose.domain.entity.language.Languages
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(private val repository: MoviesRepository){
    suspend fun execute() : Languages = repository.getLanguage()
}