package com.example.emptyactivity.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Domain model representing a single menu item with AI-generated analysis information.
 *
 * This model contains all the information about a menu item after it has been analyzed
 * by Gemini AI. It includes the item's basic information (name, description, price) as
 * well as personalized analysis results based on the user's dietary profile.
 *
 * The safety rating and tags are used by the UI to display color-coded cards and filter
 * options, helping users quickly identify safe items and items to avoid.
 *
 * @param name The name of the menu item, typically translated to the user's preferred language.
 * @param description A description of the menu item, translated and potentially enhanced
 *                    by Gemini AI with additional context.
 * @param price The price of the item as a string (e.g., "$15.99"). Can be null if
 *              price information was not available in the original menu text.
 * @param safetyRating The safety rating assigned by Gemini AI based on the user's dietary
 *                     profile. Determines the color coding in the UI (RED/YELLOW/GREEN).
 * @param allergies List of allergens found in this item that match the user's allergies.
 *                  Empty if the item is safe regarding allergies.
 * @param dietaryRestrictions List of dietary restrictions violated by this item (e.g., "vegan",
 *                            "halal"). Empty if the item complies with user's restrictions.
 * @param dislikes List of disliked ingredients found in this item. Empty if the item
 *                 doesn't contain any disliked foods.
 * @param preferences List of preferred ingredients found in this item. Used to highlight
 *                    items that match user preferences.
 * @param recommendation Optional personalized recommendation text from Gemini AI explaining
 *                       why this item is recommended or what concerns exist.
 * @param rank Optional ranking value indicating how well this item matches the user's
 *             preferences (1 = best match, higher numbers = less ideal). Used for sorting
 *             items by recommendation quality.
 */
@Serializable
data class MenuItem(
    val name: String,
    val description: String,
    val price: String? = null,
    @Serializable(with = SafetyRatingSerializer::class)
    val safetyRating: SafetyRating,
    val allergies: List<String> = emptyList(),
    val dietaryRestrictions: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val preferences: List<String> = emptyList(),
    val recommendation: String? = null,
    val rank: Int? = null,
)

/**
 * Safety rating enum for menu items based on user's dietary profile.
 *
 * This enum represents the three-tier safety classification system used throughout the app:
 * - RED: Critical safety concern - item contains allergens or violates dietary restrictions.
 *        Users should avoid these items.
 * - YELLOW: Caution recommended - item contains disliked ingredients but is not dangerous.
 *           Users may want to avoid or modify these items.
 * - GREEN: Safe and recommended - item is safe regarding allergies/restrictions and may
 *          match user preferences. These are the best choices for the user.
 *
 * The rating is assigned by Gemini AI during menu analysis and is used for color-coding
 * in the UI and filtering options.
 */
enum class SafetyRating {
    /** Item contains allergens or violates dietary restrictions - avoid */
    RED,
    /** Item contains disliked ingredients - caution recommended */
    YELLOW,
    /** Item is safe and may match preferences - recommended */
    GREEN,
}

/**
 * Custom serializer for SafetyRating enum to ensure proper JSON serialization/deserialization.
 *
 * This serializer converts the SafetyRating enum to/from string values in JSON, ensuring
 * compatibility with Gemini AI responses and database storage. The enum name is used as
 * the string representation (e.g., "RED", "YELLOW", "GREEN").
 */
object SafetyRatingSerializer : KSerializer<SafetyRating> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SafetyRating", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SafetyRating) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): SafetyRating = SafetyRating.valueOf(decoder.decodeString())
}
