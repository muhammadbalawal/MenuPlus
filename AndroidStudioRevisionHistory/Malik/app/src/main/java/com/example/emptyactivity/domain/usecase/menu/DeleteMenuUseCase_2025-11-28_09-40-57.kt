package com.example.emptyactivity.domain.usecase.menu

import com.example.emptyactivity.data.repository.menu.MenuRepository
import com.example.emptyactivity.util.Result
import javax.inject.Inject

class DeleteMenuUseCase
    @Inject
    constructor(
        private val menuRepository: MenuRepository,
    ) {
        suspend operator fun invoke(menuId: String): Result<Unit> = menuRepository.deleteMenu(menuId)
    }

