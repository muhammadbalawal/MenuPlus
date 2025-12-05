package com.example.emptyactivity.domain.usecase.menu

import com.example.emptyactivity.data.repository.menu.MenuRepository
import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for retrieving all saved menus for a specific user.
 *
 * This use case fetches all menus that have been saved by a user from the database.
 * It delegates to the MenuRepository to perform the actual data retrieval operation.
 *
 * @param menuRepository The repository interface for menu data operations. Injected via Hilt.
 *
 * Mostly created by: Malik
 */
class GetMenusUseCase
    @Inject
    constructor(
        private val menuRepository: MenuRepository,
    ) {
        /**
         * Retrieves all saved menus for a specific user.
         *
         * @param userId The unique identifier of the user whose menus should be retrieved.
         * @return Result containing a list of Menu objects on success, or an error message
         *         if the operation fails. Returns an empty list if the user has no saved menus.
         */
        suspend operator fun invoke(userId: String): Result<List<Menu>> = menuRepository.getAllMenusByUserId(userId)
    }

