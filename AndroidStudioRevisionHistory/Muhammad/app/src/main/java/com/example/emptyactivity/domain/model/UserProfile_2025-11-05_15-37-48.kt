package com.example.emptyactivity.domain.model

data class UserProfile(
    val userId: String,
    val preferredLanguageId: String,
    val preferredLanguageName: String? = null,
    val allergies: List<String> = emptyList(),
    val dietaryRestrictions: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val preferences: List<String> = emptyList(),
)
