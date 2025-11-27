package com.example.emptyactivity.ui.screens.ocr

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.domain.usecase.ocr.ExtractTextUseCase
import com.example.emptyactivity.util.ImageEncoding
import com.example.emptyactivity.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the OCR screen.
 *
 * This ViewModel manages the OCR workflow: image selection, encoding, text extraction,
 * and state management. It coordinates between the UI layer and the OCR use case to
 * extract text from user-selected images.
 *
 * The ViewModel exposes reactive StateFlows that the UI can observe to update automatically
 * when the OCR state changes (loading, success, error).
 *
 * @param extractTextUseCase The use case for extracting text from images. Injected via Hilt.
 */
@HiltViewModel
class OcrViewModel
    @Inject
    constructor(
        private val extractTextUseCase: ExtractTextUseCase,
    ) : ViewModel() {
        private val _imageUri = MutableStateFlow<Uri?>(null)
        val imageUri: StateFlow<Uri?> = _imageUri

        private val _lines = MutableStateFlow<List<String>>(emptyList())
        val lines: StateFlow<List<String>> = _lines

        private val _loading = MutableStateFlow(false)
        val loading: StateFlow<Boolean> = _loading

        private val _error = MutableStateFlow<String?>(null)
        val error: StateFlow<String?> = _error

        /**
         * Combines all extracted text lines into a single string.
         *
         * This method joins all the extracted text lines with newline characters,
         * creating a complete text document that can be passed to the Gemini analysis screen.
         *
         * @return A single string containing all extracted text lines, separated by newlines.
         *         Returns an empty string if no text has been extracted yet.
         */
        fun getExtractedText(): String = lines.value.joinToString("\n")

        /**
         * Initiates OCR extraction when a user selects an image.
         *
         * This method handles the complete OCR workflow:
         * 1. Stores the selected image URI
         * 2. Sets loading state to true
         * 3. Converts the image URI to base64 encoding
         * 4. Calls the extract text use case to extract text
         * 5. Updates the UI state with results or errors
         *
         * The operation runs in a coroutine to avoid blocking the UI thread. Any errors
         * during the process are caught and handled through the Result type.
         *
         * @param uri The Android URI of the selected image. If null, the method returns early.
         * @param context Android context needed to read the image file from the URI.
         */
        fun onImagePicked(uri: Uri?, context: Context) {
            if (uri == null) return
            _imageUri.value = uri
            _loading.value = true
            _error.value = null

            viewModelScope.launch {
                try {
                    val b64 = ImageEncoding.uriToBase64(context, uri)
                    when (val result = extractTextUseCase(b64)) {
                        is Result.Success -> {
                            _lines.value = result.data
                            _error.value = null
                        }
                        is Result.Error -> {
                            _error.value = result.message
                            _lines.value = emptyList()
                        }
                        is Result.Loading -> {
                            // Keep loading state
                        }
                    }
                } catch (t: Throwable) {
                    _error.value = t.message ?: "Failed to process image"
                    _lines.value = emptyList()
                } finally {
                    _loading.value = false
                }
            }
        }

        /**
         * Resets all OCR state to initial values.
         *
         * Clears the selected image, extracted text lines, and any error messages.
         * This is typically called when the user wants to start over with a new image.
         */
        fun clearUi() {
            _imageUri.value = null
            _lines.value = emptyList()
            _error.value = null
        }
    }