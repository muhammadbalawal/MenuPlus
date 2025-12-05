package com.example.emptyactivity.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for user profile data from Supabase.
 *
 * This DTO represents the structure of user profile data as stored in the Supabase
 * `userProfile` table. It uses @SerialName annotations to map between Kotlin property
 * names and the database column names (which use snake_case).
 *
 * Note: Dietary information (allergies, restrictions, dislikes, preferences) is stored
 * as comma-separated strings in the database, which are parsed into lists by the
 * UserProfileRepositoryImpl when converting to the domain UserProfile model.
 *
 * @param userId The unique identifier of the user (primary key in database).
 * @param preferredLanguageId The ID of the user's preferred language (foreign key to language table).
 * @param userAllergies Comma-separated string of allergens. Null if no allergies are set.
 * @param userDietaryRestrictions Comma-separated string of dietary restrictions. Null if none are set.
 * @param userDislikes Comma-separated string of disliked foods. Null if none are set.
 * @param userPreferences Comma-separated string of preferred foods. Null if none are set.
 *
 * Mostly created by: Malik
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
 * This DTO represents the structure required to insert a new user profile into the Supabase
 * `userProfile` table. It's used during onboarding to create a user's initial dietary profile.
 *
 * Dietary information is provided as comma-separated strings, which are stored directly
 * in the database columns.
 *
 * @param userId The unique identifier of the user.
 * @param preferredLanguageId The ID of the user's preferred language.
 * @param userAllergies Comma-separated string of allergens. Can be null.
 * @param userDietaryRestrictions Comma-separated string of dietary restrictions. Can be null.
 * @param userDislikes Comma-separated string of disliked foods. Can be null.
 * @param userPreferences Comma-separated string of preferred foods. Can be null.
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
