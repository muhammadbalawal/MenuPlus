package com.example.emptyactivity.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for language data from Supabase.
 *
 * This DTO represents the language data structure as stored in the Supabase database.
 * It maps the database column names (using @SerialName) to Kotlin properties and is
 * converted to the domain Language model for use throughout the application.
 *
 * @param languageId Unique identifier for the language (e.g., "en", "fr", "es").
 * @param languageName Display name of the language (e.g., "English", "French", "Spanish").
 */
@Serializable
data class LanguageDto(
    @SerialName("language_id")
    val languageId: String,
    @SerialName("language_name")
    val languageName: String,
)
