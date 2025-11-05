package com.example.emptyactivity.ui.screens.ocr

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.data.repository.ocr.OcrRepository
import com.example.emptyactivity.util.ImageEncoding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcrViewModel @Inject constructor(
    private val repo: OcrRepository
) : ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private val _lines = MutableStateFlow<List<String>>(emptyList())
    val lines: StateFlow<List<String>> = _lines

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onImagePicked(uri: Uri?, context: Context) {
        if (uri == null) return
        _imageUri.value = uri
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val b64 = ImageEncoding.uriToBase64(context, uri)
                val result = repo.extractLines(b64)
                _lines.value = result
            } catch (t: Throwable) {
                _error.value = t.message ?: "OCR failed"
            } finally {
                _loading.value = false
            }
        }
    }

    // <-- Name must NOT be `clear()`, that collides with ViewModel internal API.
    fun clearUi() {
        _imageUri.value = null
        _lines.value = emptyList()
        _error.value = null
    }
}
