package com.example.emptyactivity.domain.model

/**
 * Domain model representing a supported language.
 *
 * This model represents a language that users can select as their preferred language for
 * menu translation and UI personalization. Languages are stored in the database and fetched
 * during onboarding and profile editing.
 *
 * @param id The unique identifier of the language (e.g., "en", "fr", "es").
 * @param name The display name of the language (e.g., "English", "French", "Spanish").
 */
data class Language(
    val id: String,
    val name: String,
)
