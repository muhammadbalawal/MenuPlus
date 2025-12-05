package com.example.emptyactivity.domain.usecase.menu

import com.example.emptyactivity.data.repository.menu.MenuRepository
import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.util.Result
import javax.inject.Inject

class GetMenusUseCase
    @Inject
    constructor(
        private val menuRepository: MenuRepository,
    ) {
        suspend operator fun invoke(userId: String): Result<List<Menu>> = menuRepository.getAllMenusByUserId(userId)
    }

