package com.example.emptyactivity.data.repository.menu

import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.util.Result

/**
 * Repository interface for menu data operations.
 *
 * This repository abstracts menu persistence operations, allowing the app to save, retrieve,
 * and delete analyzed menus without directly depending on the specific database implementation
 * (currently Supabase).
 *
 * The repository pattern allows us to:
 * - Switch database providers in the future without changing business logic
 * - Easily mock this interface for testing
 * - Keep database implementation details separate from the domain layer
 *
 * All operations return Result types for consistent error handling across the application.
 *
 * Mostly created by: Malik
 */
interface MenuRepository {
    /**
     * Saves a menu to the database.
     *
     * This method persists a menu with its analyzed items (as JSON) and optional image URI
     * to the database. The menu is associated with the specified user ID.
     *
     * @param userId The unique identifier of the user saving the menu.
     * @param menuText The original OCR-extracted menu text.
     * @param menuItemsJson JSON string containing serialized menu items. Can be null if
     *                      menu items haven't been analyzed yet.
     * @param imageUri Optional URI string of the original menu image. Can be null if no
     *                 image was provided.
     * @return Result containing the saved Menu object with generated menuId, or an error
     *         if the save operation fails.
     */
    suspend fun saveMenu(
        userId: String,
        menuText: String,
        menuItemsJson: String?,
        imageUri: String?,
    ): Result<Menu>

    /**
     * Retrieves a menu by its unique identifier.
     *
     * @param menuId The unique identifier of the menu to retrieve.
     * @return Result containing the Menu object if found, null if not found, or an error
     *         if the operation fails.
     */
    suspend fun getMenuById(menuId: String): Result<Menu?>

    /**
     * Retrieves all menus saved by a specific user.
     *
     * @param userId The unique identifier of the user whose menus should be retrieved.
     * @return Result containing a list of Menu objects, or an error if the operation fails.
     *         Returns an empty list if the user has no saved menus.
     */
    suspend fun getAllMenusByUserId(userId: String): Result<List<Menu>>

    /**
     * Deletes a menu from the database.
     *
     * @param menuId The unique identifier of the menu to delete.
     * @return Result indicating success (Unit) or an error if the operation fails.
     */
    suspend fun deleteMenu(menuId: String): Result<Unit>
}
