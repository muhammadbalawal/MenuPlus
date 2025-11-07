package com.example.emptyactivity.ui.screens.importmenu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.domain.usecase.menu.AnalyzeMenuUseCase
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportMenuViewModel
    @Inject
    constructor(
        private val analyzeMenuUseCase: AnalyzeMenuUseCase,
    ) : ViewModel() {
        companion object {
            private const val TAG = "ImportMenuViewModel"
        }

        private val _uiState = MutableStateFlow(ImportMenuUiState())
        val uiState: StateFlow<ImportMenuUiState> = _uiState.asStateFlow()

        fun onMenuTextChange(text: String) {
            _uiState.update { it.copy(menuText = text, errorMessage = null) }
        }

        fun onAnalyzeMenu(user: User) {
            viewModelScope.launch {
                Log.d(TAG, "Starting menu analysis for user: ${user.id}")
                _uiState.update {
                    it.copy(
                        isAnalyzing = true,
                        errorMessage = null,
                        analysisResult = null,
                    )
                }

                val menuText = uiState.value.menuText

                when (
                    val result =
                        analyzeMenuUseCase(
                            userId = user.id,
                            menuText = menuText,
                        )
                ) {
                    is Result.Success -> {
                        Log.d(TAG, "Menu analysis successful")
                        _uiState.update {
                            it.copy(
                                isAnalyzing = false,
                                analysisResult = result.data,
                            )
                        }
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Menu analysis failed: ${result.message}")
                        _uiState.update {
                            it.copy(
                                isAnalyzing = false,
                                errorMessage = result.message,
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isAnalyzing = true) }
                    }
                }
            }
        }

        fun onErrorDismissed() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        fun onClearResult() {
            _uiState.update { it.copy(analysisResult = null) }
        }
    }
