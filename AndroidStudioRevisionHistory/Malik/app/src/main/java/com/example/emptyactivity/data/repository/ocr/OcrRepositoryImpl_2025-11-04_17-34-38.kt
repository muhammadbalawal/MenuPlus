package com.example.emptyactivity.data.repository.ocr

import com.example.emptyactivity.data.remote.vision.*

class OcrRepositoryImpl(
    private val api: VisionApi
) : OcrRepository {

    override suspend fun extractLines(base64Image: String): List<String> {
        val req = VisionRequest(
            requests = listOf(
                AnnotateImageRequest(
                    image = VisionImage(base64Image)
                )
            )
        )
        val resp = api.annotate(req)
        val raw = resp.responses.firstOrNull()?.fullTextAnnotation?.text.orEmpty()
        return raw.split('\n').map { it.trim() }.filter { it.isNotBlank() }
    }
}
