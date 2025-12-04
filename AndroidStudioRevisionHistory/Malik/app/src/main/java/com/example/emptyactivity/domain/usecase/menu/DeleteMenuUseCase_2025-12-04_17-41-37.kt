package com.example.emptyactivity.domain.usecase.menu

import com.example.emptyactivity.data.repository.menu.MenuRepository
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for deleting a saved menu from the database.
 *
 * This use case handles the deletion of a menu that the user no longer wants to keep.
 * It delegates to the MenuRepository to perform the actual deletion operation.
 *
 * @param menuRepository The repository interface for menu data operations. Injected via Hilt.
 */
class DeleteMenuUseCase
    @Inject
    constructor(
        private val menuRepository: MenuRepository,
    ) {
        /**
         * Deletes a menu from the database.
         *
         * @param menuId The unique identifier of the menu to delete.
         * @return Result indicating success (Unit) or an error message if the operation fails.
         */
        suspend operator fun invoke(menuId: String): Result<Unit> = menuRepository.deleteMenu(menuId)
    }

