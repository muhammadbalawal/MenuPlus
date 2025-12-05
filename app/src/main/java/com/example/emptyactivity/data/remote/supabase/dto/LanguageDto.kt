package com.example.emptyactivity.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for language data from Supabase.
 *
 * This DTO represents the structure of language data as stored in the Supabase `language` table.
 * It uses @SerialName annotations to map between Kotlin property names and the database column
 * names (which use snake_case).
 *
 * This DTO is used for deserializing language data from Supabase responses and is converted to
 * the domain Language model by the UserProfileRepositoryImpl.
 *
 * @param languageId The unique identifier of the language (e.g., "en", "fr", "es").
 * @param languageName The display name of the language (e.g., "English", "French", "Spanish").
 *
 * Mostly created by: Malik
 */
@Serializable
data class LanguageDto(
    @SerialName("language_id")
    val languageId: String,
    @SerialName("language_name")
    val languageName: String,
)
