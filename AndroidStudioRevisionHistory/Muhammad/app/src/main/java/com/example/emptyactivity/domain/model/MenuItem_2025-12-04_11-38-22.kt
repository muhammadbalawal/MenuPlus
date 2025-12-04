package com.example.emptyactivity.domain.model

/**
 * Represents a single menu item with analysis information.
 */
data class MenuItem(
    val name: String,
    val description: String,
    val price: String? = null,
    val safetyRating: SafetyRating,
    val allergies: List<String> = emptyList(),
    val dietaryRestrictions: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val preferences: List<String> = emptyList(),
    val recommendation: String? = null,
    val rank: Int? = null, // For ordering by best for user (1 = best)
)

/**
 * Safety rating for menu items based on user's dietary profile.
 */
enum class SafetyRating {
    RED,    // Contains allergies/restrictions - avoid
    YELLOW, // Contains dislikes - caution
    GREEN   // Safe to eat
}
