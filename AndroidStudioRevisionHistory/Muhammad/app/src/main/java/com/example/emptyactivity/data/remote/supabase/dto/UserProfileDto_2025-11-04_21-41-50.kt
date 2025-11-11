package com.example.emptyactivity.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val userPreferences: String? = null
)

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
    val userPreferences: String? = null
)