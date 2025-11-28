package com.example.emptyactivity.data.repository.menu

import android.util.Log
import com.example.emptyactivity.data.remote.supabase.SupabaseClientProvider
import com.example.emptyactivity.data.remote.supabase.dto.CreateMenuDto
import com.example.emptyactivity.data.remote.supabase.dto.MenuDto
import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.util.Result
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuRepositoryImpl
    @Inject
    constructor() : MenuRepository {
        private val supabase = SupabaseClientProvider.client

        companion object {
            private const val TAG = "MenuRepository"
            private const val TABLE_MENU = "user_menu" 
        }

        override suspend fun saveMenu(
            userId: String,
            menuText: String,
            safeMenuContent: String?,
            bestMenuContent: String?,
            fullMenuContent: String?,
            imageUri: String?,
        ): Result<Menu> =
            try {
                Log.d(TAG, "Saving menu for userId: $userId")

                val createDto =
                    CreateMenuDto(
                        userId = userId,
                        menuText = menuText,
                        safeMenuContent = safeMenuContent,
                        bestMenuContent = bestMenuContent,
                        fullMenuContent = fullMenuContent,
                        imageUri = imageUri,
                    )

                supabase
                    .from(TABLE_MENU)
                    .insert(createDto)

                Log.d(TAG, "Menu saved successfully")

                // Fetch the saved menu to return
                val menus =
                    supabase
                        .from(TABLE_MENU)
                        .select(
                            columns =
                                Columns.raw(
                                    """
                                    menu_id,
                                    user_id,
                                    menu_text,
                                    safe_menu_content,
                                    best_menu_content,
                                    full_menu_content,
                                    image_uri,
                                    created_at
                                    """.trimIndent(),
                                ),
                        ) {
                            filter {
                                eq("user_id", userId)
                            }
                            limit(1)
                        }.decodeList<MenuDto>()

                if (menus.isEmpty()) {
                    Result.Error("Menu saved but failed to fetch")
                } else {
                    val menuDto = menus.first()
                    Result.Success(menuDto.toDomain())
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saving menu", e)
                Result.Error("Failed to save menu: ${e.message}", e)
            }

        override suspend fun getMenuById(menuId: String): Result<Menu?> =
            try {
                Log.d(TAG, "Fetching menu by id: $menuId")

                val menus =
                    supabase
                        .from(TABLE_MENU)
                        .select(
                            columns =
                                Columns.raw(
                                    """
                                    menu_id,
                                    user_id,
                                    menu_text,
                                    safe_menu_content,
                                    best_menu_content,
                                    full_menu_content,
                                    image_uri,
                                    created_at
                                    """.trimIndent(),
                                ),
                        ) {
                            filter {
                                eq("menu_id", menuId)
                            }
                        }.decodeList<MenuDto>()

                if (menus.isEmpty()) {
                    Log.d(TAG, "No menu found with id: $menuId")
                    Result.Success(null)
                } else {
                    val menuDto = menus.first()
                    Result.Success(menuDto.toDomain())
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching menu", e)
                Result.Error("Failed to load menu: ${e.message}", e)
            }

        override suspend fun getAllMenusByUserId(userId: String): Result<List<Menu>> =
            try {
                Log.d(TAG, "Fetching all menus for userId: $userId")

                val menus =
                    supabase
                        .from(TABLE_MENU)
                        .select(
                            columns =
                                Columns.raw(
                                    """
                                    menu_id,
                                    user_id,
                                    menu_text,
                                    safe_menu_content,
                                    best_menu_content,
                                    full_menu_content,
                                    image_uri,
                                    created_at
                                    """.trimIndent(),
                                ),
                        ) {
                            filter {
                                eq("user_id", userId)
                            }
                        }.decodeList<MenuDto>()

                Log.d(TAG, "Fetched ${menus.size} menus")

                val menuModels = menus.map { it.toDomain() }
                Result.Success(menuModels)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching menus", e)
                Result.Error("Failed to load menus: ${e.message}", e)
            }

        override suspend fun deleteMenu(menuId: String): Result<Unit> =
            try {
                Log.d(TAG, "Deleting menu: $menuId")

                supabase
                    .from(TABLE_MENU)
                    .delete {
                        filter {
                            eq("menu_id", menuId)
                        }
                    }

                Log.d(TAG, "Menu deleted successfully")
                Result.Success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting menu", e)
                Result.Error("Failed to delete menu: ${e.message}", e)
            }

        private fun MenuDto.toDomain(): Menu {
            val createdAtLong =
                createdAt?.let {
                    try {
                        // Parse ISO 8601 timestamp from Supabase
                        val formatter =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
                        formatter.timeZone = TimeZone.getTimeZone("UTC")
                        formatter.parse(it)?.time ?: System.currentTimeMillis()
                    } catch (e: Exception) {
                        System.currentTimeMillis()
                    }
                } ?: System.currentTimeMillis()

            return Menu(
                menuId = this.menuId,
                userId = this.userId,
                menuText = this.menuText,
                safeMenuContent = this.safeMenuContent,
                bestMenuContent = this.bestMenuContent,
                fullMenuContent = this.fullMenuContent,
                imageUri = this.imageUri,
                createdAt = createdAtLong,
            )
        }
    }