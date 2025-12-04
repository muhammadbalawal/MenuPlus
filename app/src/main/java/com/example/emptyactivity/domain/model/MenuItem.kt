package com.example.emptyactivity.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Represents a single menu item with analysis information.
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
    val rank: Int? = null, // For ordering by best for user (1 = best)
)

/**
 * Safety rating for menu items based on user's dietary profile.
 */
enum class SafetyRating {
    RED, // Contains allergies/restrictions - avoid
    YELLOW, // Contains dislikes - caution
    GREEN, // Safe to eat
}

object SafetyRatingSerializer : KSerializer<SafetyRating> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SafetyRating", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SafetyRating) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): SafetyRating = SafetyRating.valueOf(decoder.decodeString())
}
