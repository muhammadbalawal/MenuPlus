package com.example.emptyactivity.ui.screens.ocr

import android.net.Uri

/**
 * UI state model for the OCR screen.
 *
 * This data class holds all the state information needed to render the OCR screen UI.
 * It tracks the selected image, extracted text lines, loading status, and any errors
 * that occur during the OCR process.
 *
 * @param imageUri The URI of the image selected by the user. Null if no image has been selected yet.
 * @param lines List of text lines extracted from the image. Empty until OCR completes successfully.
 * @param loading True when OCR extraction is in progress, false otherwise.
 * @param error Error message string if OCR fails. Null if no error has occurred.
 *
 * Mostly created by: Malik
 */
data class OcrUiState(
    val imageUri: Uri? = null,
    val lines: List<String> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)
