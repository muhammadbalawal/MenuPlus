package com.example.emptyactivity.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for user profile data from Supabase.
 *
 * This DTO represents the user profile data structure as stored in the Supabase database.
 * It maps the database column names (using @SerialName) to Kotlin properties. The dietary
 * preferences are stored as comma-separated strings in the database and converted to
 * lists in the domain model.
 *
 * @param userId The unique identifier of the user this profile belongs to.
 * @param preferredLanguageId The ID of the user's preferred language.
 * @param userAllergies Comma-separated string of allergies, or null if none.
 * @param userDietaryRestrictions Comma-separated string of dietary restrictions, or null if none.
 * @param userDislikes Comma-separated string of dislikes, or null if none.
 * @param userPreferences Comma-separated string of preferences, or null if none.
 */
@Serializable
data class UserProfileDto(
    @SerialName("user_id")
    val userId: String,
    @SerialName("preferred_language")
    val preferredLanguageId: String,
    @SerialName("user_allergies")
    val userAllergies: String? = null,
    @SerialName("user_dietary_restrictions")
    val userDietaryRestrictions: String? = null,
    @SerialName("user_dislikes")
    val userDislikes: String? = null,
    @SerialName("user_preferences")
    val userPreferences: String? = null,
)

/**
 * Data Transfer Object (DTO) for creating a new user profile in Supabase.
 *
 * This DTO is used when inserting a new user profile into the database. It has the same
 * structure as UserProfileDto but is used specifically for create operations.
 *
 * @param userId The unique identifier of the user this profile belongs to.
 * @param preferredLanguageId The ID of the user's preferred language.
 * @param userAllergies Comma-separated string of allergies, or null if none.
 * @param userDietaryRestrictions Comma-separated string of dietary restrictions, or null if none.
 * @param userDislikes Comma-separated string of dislikes, or null if none.
 * @param userPreferences Comma-separated string of preferences, or null if none.
 */
@Serializable
data class CreateUserProfileDto(
    @SerialName("user_id")
    val userId: String,
    @SerialName("preferred_language")
    val preferredLanguageId: String,
    @SerialName("user_allergies")
    val userAllergies: String? = null,
    @SerialName("user_dietary_restrictions")
    val userDietaryRestrictions: String? = null,
    @SerialName("user_dislikes")
    val userDislikes: String? = null,
    @SerialName("user_preferences")
    val userPreferences: String? = null,
)
