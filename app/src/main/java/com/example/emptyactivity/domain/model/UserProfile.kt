package com.example.emptyactivity.domain.model

/**
 * Domain model representing a user's dietary profile and preferences.
 *
 * This model contains all the dietary information needed for personalized menu analysis.
 * It's stored in Supabase and used by Gemini AI to provide safety ratings and recommendations.
 *
 * @param userId The unique identifier of the user this profile belongs to.
 * @param preferredLanguageId The ID of the user's preferred language (e.g., "en", "fr").
 * @param preferredLanguageName The display name of the preferred language (e.g., "English", "French").
 *                               Used for menu translation and UI display.
 * @param allergies List of allergens the user must avoid. Critical for safety - items containing
 *                  these will be marked RED in menu analysis.
 * @param dietaryRestrictions List of dietary restrictions (e.g., "vegan", "halal", "kosher", "vegetarian").
 *                           Items violating these will be marked RED.
 * @param dislikes List of foods the user prefers not to eat. Items containing these will be
 *                 marked YELLOW (caution) in menu analysis.
 * @param preferences List of foods the user enjoys. Used to provide personalized recommendations
 *                    and mark items as GREEN (safe and recommended).
 *
 * Mostly created by: Malik
 */
data class UserProfile(
    val userId: String,
    val preferredLanguageId: String,
    val preferredLanguageName: String? = null,
    val allergies: List<String> = emptyList(),
    val dietaryRestrictions: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val preferences: List<String> = emptyList(),
)
