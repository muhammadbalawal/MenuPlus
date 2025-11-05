package com.example.emptyactivity.ui.screens.ocr

import android.net.Uri

data class OcrUiState(
    val imageUri: Uri? = null,
    val lines: List<String> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
