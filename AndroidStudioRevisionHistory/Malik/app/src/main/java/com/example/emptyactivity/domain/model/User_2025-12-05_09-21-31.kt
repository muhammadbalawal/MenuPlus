package com.example.emptyactivity.domain.model

/**
 * Domain model representing an authenticated user.
 *
 * This model contains the core user information needed throughout the application.
 * It's used to identify the current user and determine navigation flow (onboarding vs. main app).
 *
 * @param id Unique identifier for the user, typically from Supabase Auth.
 * @param email User's email address used for authentication.
 * @param name Optional display name for the user. May be null if not set.
 * @param hasCompletedOnboarding True if the user has completed the dietary profile setup,
 *                               false if they need to go through onboarding.
 * @param createdAt Timestamp of when the user account was created (milliseconds since epoch).
 *
 * Mostly created by: Muhammad
 */
data class User(
    val id: String,
    val email: String,
    val name: String? = null,
    val hasCompletedOnboarding: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)
