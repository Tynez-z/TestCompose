package com.example.testCompose.domain.entity.language

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class LanguageItem(
    val iso_639_1: String,
    val english_name: String,
    val name: String
) {
    companion object {
        @SuppressLint("ConstantLocale")
        val default = LanguageItem(
            english_name = Locale.getDefault().displayLanguage,
            iso_639_1 = Locale.getDefault().language,
            name = Locale.getDefault().displayLanguage
        )
    }
}