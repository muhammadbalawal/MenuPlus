package com.example.emptyactivity.domain.usecase.menu

import com.example.emptyactivity.data.repository.menu.MenuRepository
import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.util.Result
import javax.inject.Inject

class GetMenuByIdUseCase
    @Inject
    constructor(
        private val menuRepository: MenuRepository,
    ) {
        suspend operator fun invoke(menuId: String): Result<Menu?> {
            return menuRepository.getMenuById(menuId)
        }
    }

