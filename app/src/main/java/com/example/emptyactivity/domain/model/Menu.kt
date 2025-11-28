package com.example.emptyactivity.domain.model

/**
 * Domain model representing a saved menu analysis.
 *
 * This model contains all the information about a menu that has been analyzed and saved.
 * It includes the original menu text, the analysis results (safe, best, full menu),
 * and metadata about when it was created.
 *
 * @param menuId The unique identifier of the menu.
 * @param userId The unique identifier of the user who saved this menu.
 * @param menuText The original OCR-extracted menu text.
 * @param safeMenuContent The analyzed safe menu content (GREEN items).
 * @param bestMenuContent The analyzed best menu content (recommendations).
 * @param fullMenuContent The analyzed full menu content (all items with ratings).
 * @param imageUri Optional URI string of the original menu image.
 * @param createdAt Timestamp when the menu was saved (milliseconds since epoch).
 */
data class Menu(
    val menuId: String,
    val userId: String,
    val menuText: String,
    val safeMenuContent: String? = null,
    val bestMenuContent: String? = null,
    val fullMenuContent: String? = null,
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)