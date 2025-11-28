package com.example.emptyactivity.ui.screens.menuanalysis

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.domain.usecase.menu.SaveMenuUseCase
import com.example.emptyactivity.util.ImageEncoding
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuAnalysisViewModel
    @Inject
    constructor(
        private val saveMenuUseCase: SaveMenuUseCase,
    ) : ViewModel() {
        companion object {
            private const val TAG = "MenuAnalysisViewModel"
        }

        private val _uiState = MutableStateFlow(MenuAnalysisUiState())
        val uiState: StateFlow<MenuAnalysisUiState> = _uiState.asStateFlow()

        fun onSaveMenu(
            user: User,
            menuText: String,
            safeMenuContent: String?,
            bestMenuContent: String?,
            fullMenuContent: String?,
            imageUriString: String = "",
            context: Context? = null,
        ) {
            viewModelScope.launch {
                Log.d(TAG, "Saving menu for user: ${user.id}")
                _uiState.update {
                    it.copy(
                        isSaving = true,
                        errorMessage = null,
                        isSaved = false,
                    )
                }

                var imageBase64: String? = null
                if (imageUriString.isNotBlank() && context != null) {
                    try {
                        val uri = Uri.parse(imageUriString)
                        imageBase64 = ImageEncoding.uriToBase64(context, uri)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error encoding image to base64", e)
                    }
                }

                when (
                    val result =
                        saveMenuUseCase(
                            userId = user.id,
                            menuText = menuText,
                            safeMenuContent = safeMenuContent,
                            bestMenuContent = bestMenuContent,
                            fullMenuContent = fullMenuContent,
                            imageBase64 = imageBase64,
                        )
                ) {
                    is Result.Success -> {
                        Log.d(TAG, "Menu saved successfully")
                        _uiState.update {
                            it.copy(
                                isSaving = false,
                                isSaved = true,
                            )
                        }
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Menu save failed: ${result.message}")
                        _uiState.update {
                            it.copy(
                                isSaving = false,
                                errorMessage = result.message,
                                isSaved = false,
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isSaving = true) }
                    }
                }
            }
        }

        fun onErrorDismissed() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
