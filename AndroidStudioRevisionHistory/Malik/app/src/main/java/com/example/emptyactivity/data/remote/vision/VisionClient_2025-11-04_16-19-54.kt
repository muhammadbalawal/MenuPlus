package com.example.emptyactivity.data.remote.vision

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Builds and provides a Retrofit client for the Google Cloud Vision API.
 * The API key is injected via DI, not hardcoded.
 */
object VisionClient {
    fun create(apiKey: String): VisionApi {
        val keyInterceptor =
            Interceptor { chain ->
                val url =
                    chain
                        .request()
                        .url
                        .newBuilder()
                        .addQueryParameter("key", apiKey)
                        .build()
                val request =
                    chain
                        .request()
                        .newBuilder()
                        .url(url)
                        .build()
                chain.proceed(request)
            }

        val httpClient =
            OkHttpClient
                .Builder()
                .addInterceptor(keyInterceptor)
                .build()

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://vision.googleapis.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient)
                .build()

        return retrofit.create(VisionApi::class.java)
    }
}
