package com.example.emptyactivity.domain.usecase.menu

import android.util.Log
import com.example.emptyactivity.data.repository.menu.MenuRepository
import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.domain.model.MenuItem
import com.example.emptyactivity.util.Result
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Use case for saving analyzed menus to the database.
 *
 * This use case handles the persistence of menu analysis results to Supabase. It serializes
 * the analyzed menu items to JSON format and saves them along with the original menu text
 * and optional image URI for later retrieval.
 *
 * The use case performs validation to ensure menu text is not empty before attempting to save.
 * Menu items are serialized to JSON using Kotlin Serialization for efficient storage.
 *
 * @param menuRepository The repository interface for menu data operations. Injected via Hilt.
 *
 * Mostly created by: Muhammad
 */
class SaveMenuUseCase
    @Inject
    constructor(
        private val menuRepository: MenuRepository,
    ) {
        companion object {
            private const val TAG = "SaveMenuUseCase"
        }

        /**
         * Saves an analyzed menu to the database for later access.
         *
         * This method serializes the menu items to JSON and persists the menu data to Supabase.
         * The saved menu can be retrieved later from the SavedMenuScreen, allowing users to
         * review previously analyzed menus without re-scanning.
         *
         * @param userId The unique identifier of the user saving the menu. Used to associate
         *               the menu with the correct user account.
         * @param menuText The original OCR-extracted menu text that was analyzed.
         * @param menuItems The list of analyzed menu items with safety ratings and recommendations.
         *                  These will be serialized to JSON for storage.
         * @param imageUri Optional URI string of the original menu image. Can be null if no
         *                 image was provided or if the menu was analyzed from text only.
         * @return Result containing the saved Menu object on success, or an error message
         *         if validation fails or the save operation fails.
         */
        suspend operator fun invoke(
            userId: String,
            menuText: String,
            menuItems: List<MenuItem>,
            imageUri: String?,
        ): Result<Menu> {
            // Validation
            if (menuText.isBlank()) {
                return Result.Error("Menu text cannot be empty")
            }

            Log.d(TAG, "Saving menu for userId: $userId")

            // Serialize menuItems to JSON
            val menuItemsJson =
                try {
                    val json = Json { encodeDefaults = true }
                    json.encodeToString(menuItems)
                } catch (e: Exception) {
                    Log.e(TAG, "Error serializing menu items", e)
                    null
                }

            return menuRepository.saveMenu(
                userId = userId,
                menuText = menuText,
                menuItemsJson = menuItemsJson,
                imageUri = imageUri,
            )
        }
    }
