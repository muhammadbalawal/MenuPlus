package com.example.emptyactivity.domain.model

/**
 * Domain model representing a user in the application.
 *
 * This data class represents the core user entity with authentication and profile information.
 * It is used throughout the application to represent the current user's state and is derived
 * from Supabase authentication data.
 *
 * @param id The unique identifier for the user (from Supabase).
 * @param email The user's email address used for authentication.
 * @param name The user's display name, nullable if not set.
 * @param hasCompletedOnboarding Whether the user has completed the onboarding flow.
 * @param createdAt Timestamp of when the user account was created (in milliseconds since epoch).
 */
data class User(
    val id: String,
    val email: String,
    val name: String? = null,
    val hasCompletedOnboarding: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)
