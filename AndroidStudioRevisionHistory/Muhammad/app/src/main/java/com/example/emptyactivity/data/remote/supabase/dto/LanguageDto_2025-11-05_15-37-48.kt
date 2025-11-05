package com.example.emptyactivity.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageDto(
    @SerialName("language_id")
    val languageId: String,
    @SerialName("language_name")
    val languageName: String,
)
