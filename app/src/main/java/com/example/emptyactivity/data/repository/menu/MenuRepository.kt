package com.example.emptyactivity.data.repository.menu

import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.util.Result

interface MenuRepository {
    suspend fun saveMenu(
        userId: String,
        menuText: String,
        safeMenuContent: String?,
        bestMenuContent: String?,
        fullMenuContent: String?,
        imageUri: String?,
    ): Result<Menu>

    suspend fun getMenuById(menuId: String): Result<Menu?>

    suspend fun getAllMenusByUserId(userId: String): Result<List<Menu>>

    suspend fun deleteMenu(menuId: String): Result<Unit>
}
