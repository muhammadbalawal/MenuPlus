package com.example.emptyactivity.domain.model

/**
 * Domain model representing a language option.
 *
 * This model represents a language that users can select as their preferred language
 * for menu translation. Languages are stored in Supabase and fetched during onboarding.
 *
 * @param id Unique identifier for the language (e.g., "en", "fr", "es").
 * @param name Display name of the language (e.g., "English", "French", "Spanish").
 */
data class Language(
    val id: String,
    val name: String,
)
