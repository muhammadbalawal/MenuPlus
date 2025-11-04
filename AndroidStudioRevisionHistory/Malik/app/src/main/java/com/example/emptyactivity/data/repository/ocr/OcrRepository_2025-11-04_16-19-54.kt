package com.example.emptyactivity.data.repository.ocr

interface OcrRepository {
    suspend fun extractLines(base64Image: String): List<String>
}
