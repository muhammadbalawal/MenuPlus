package com.example.emptyactivity.domain.model

/**
 * Domain model representing a saved menu analysis.
 *
 * This model contains all the information about a menu that has been analyzed and saved.
 * It includes the original menu text, the analysis results as menu items,
 * and metadata about when it was created.
 *
 * @param menuId The unique identifier of the menu.
 * @param userId The unique identifier of the user who saved this menu.
 * @param menuText The original OCR-extracted menu text.
 * @param menuItemsJson JSON string of analyzed menu items (List<MenuItem>).
 * @param imageUri Optional URI string of the original menu image.
 * @param createdAt Timestamp when the menu was saved (milliseconds since epoch).
 */
data class Menu(
    val menuId: String,
    val userId: String,
    val menuText: String,
    val menuItemsJson: String? = null,
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)
