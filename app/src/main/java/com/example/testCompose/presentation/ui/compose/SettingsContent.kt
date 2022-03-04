package com.example.testCompose.presentation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testCompose.domain.entity.language.LanguageItem
import com.example.testCompose.presentation.viewModel.LanguageViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import testCompose.R

const val SETTINGS_DIALOG_TAG = "SettingsDialog"
private const val FLAG_ID = "flag"
private const val TICK_ID = "tickIcon"
private const val DROPDOWN_ID = "dropdownIcon"

private val placeholder = Placeholder(
    width = 2.5.em,
    height = 1.5.em,
    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
)

@Composable
fun SettingsContent(onDialogDismiss: () -> Unit) {
    val settingsViewModel = hiltViewModel<LanguageViewModel>()
    settingsViewModel.fetchLanguages()
    val uiState = settingsViewModel.uiState.collectAsState().value
    val selectedLanguage = settingsViewModel.selectedLanguage.collectAsState(initial = LanguageItem.default)
    SettingsDialog(
        uiState,
        selectedLanguage,
        onDialogDismiss = onDialogDismiss,
        onLanguageSelected = { settingsViewModel.onLanguageSelected(it) }
    )
}

@Composable
fun SettingsDialog(
    uiState: LanguageViewModel.UiState,
    selectedLanguage: State<LanguageItem>,
    onLanguageSelected: (LanguageItem) -> Unit,
    onDialogDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDialogDismiss) {
        Card(
            shape = MaterialTheme.shapes.medium,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.surface,
            modifier = Modifier
                .fillMaxWidth()
//                .semantics { testTag = SETTINGS_DIALOG_TAG }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (uiState.showLoading) {
                    LoadingRow(title = stringResource(R.string.fetch_language))
                } else {
                    LanguageRow(uiState.languages, selectedLanguage.value, onLanguageSelected)
                }
            }
        }
    }
}

@Composable
private fun LanguageRow(
    languages: List<LanguageItem>,
    selectedLanguage: LanguageItem,
    onLanguageSelected: (LanguageItem) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var showDropdown by remember { mutableStateOf(false) }
        Text(stringResource(R.string.language))
        DropdownMenu(
            expanded = showDropdown,
            modifier = Modifier.fillMaxHeight(0.4f),
            onDismissRequest = { showDropdown = false }
        ) {
            languages.forEach { language ->
                val selected = language == selectedLanguage
                DropdownItem(language.english_name,selected) {
                    onLanguageSelected(language)
                    showDropdown = false
                }
            }
        }
        ToggleContent(selectedLanguage.english_name) {
            showDropdown = true
        }
    }
}

@Composable
private fun ToggleContent(countryName: String,onClick: () -> Unit) {
    val arrowContent = iconContent(DROPDOWN_ID, Icons.Default.ArrowDropDown)
    Text(
        text = buildAnnotatedString {
//            appendInlineContent(FLAG_ID)
            append("  $countryName")
            appendInlineContent(DROPDOWN_ID)
        },
        inlineContent = mapOf(arrowContent),
        modifier = Modifier.clickable(onClick = onClick),
        color = Color.Black
    )
}

@Composable
private fun DropdownItem(countryName: String, selected: Boolean, onClick: () -> Unit) {
    DropdownMenuItem(enabled = !selected, onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = buildAnnotatedString {
                if (selected) {
                    appendInlineContent(TICK_ID)
                }
//                appendInlineContent(FLAG_ID)
                append("  $countryName")
            },
            inlineContent = inlineContent(countryName, selected)
        )
    }
}

private fun inlineContent(countryName: String, selected: Boolean): Map<String, InlineTextContent> {
    return if (selected) mapOf(iconContent(TICK_ID, Icons.Default.Done)) else mapOf()
}

private fun iconContent(id: String, icon: ImageVector) = id to InlineTextContent(
    placeholder = placeholder,
    children = {
        Image(
            imageVector = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
        )
    }
)


@Composable
fun LoadingRow(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(modifier = Modifier.size(40.dp))
        Text(title)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoadingRowPreview() {
    LoadingRow(title = "Please wait...")
}