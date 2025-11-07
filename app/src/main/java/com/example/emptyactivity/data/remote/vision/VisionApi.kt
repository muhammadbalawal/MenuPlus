package com.example.emptyactivity.data.remote.vision


import retrofit2.http.Body
import retrofit2.http.POST

interface VisionApi {
    @POST("v1/images:annotate")
    suspend fun annotate(
        @Body request: VisionRequest,
    ): VisionResponse
}
