package com.example.emptyactivity.domain.usecase.menu

import android.util.Log
import com.example.emptyactivity.data.repository.menu.MenuRepository
import com.example.emptyactivity.domain.model.Menu
import com.example.emptyactivity.util.Result
import javax.inject.Inject

class SaveMenuUseCase
    @Inject
    constructor(
        private val menuRepository: MenuRepository,
    ) {
        companion object {
            private const val TAG = "SaveMenuUseCase"
        }

        suspend operator fun invoke(
            userId: String,
            menuText: String,
            safeMenuContent: String?,
            bestMenuContent: String?,
            fullMenuContent: String?,
            imageUri: String?,
        ): Result<Menu> {
            // Validation
            if (menuText.isBlank()) {
                return Result.Error("Menu text cannot be empty")
            }

            Log.d(TAG, "Saving menu for userId: $userId")

            return menuRepository.saveMenu(
                userId = userId,
                menuText = menuText,
                safeMenuContent = safeMenuContent,
                bestMenuContent = bestMenuContent,
                fullMenuContent = fullMenuContent,
                imageUri = imageUri,
            )
        }
    }
