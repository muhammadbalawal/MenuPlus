package com.example.emptyactivity.domain.usecase.menu

import com.example.emptyactivity.data.repository.menu.MenuRepository
import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for retrieving a specific menu by its unique identifier.
 *
 * This use case fetches a single menu from the database using its menu ID. It's typically
 * used when displaying detailed information about a saved menu in the SavedMenuDetailScreen.
 *
 * @param menuRepository The repository interface for menu data operations. Injected via Hilt.
 */
class GetMenuByIdUseCase
    @Inject
    constructor(
        private val menuRepository: MenuRepository,
    ) {
        /**
         * Retrieves a specific menu by its unique identifier.
         *
         * @param menuId The unique identifier of the menu to retrieve.
         * @return Result containing the Menu object if found, null if not found, or an error
         *         message if the operation fails.
         */
        suspend operator fun invoke(menuId: String): Result<Menu?> = menuRepository.getMenuById(menuId)
    }

