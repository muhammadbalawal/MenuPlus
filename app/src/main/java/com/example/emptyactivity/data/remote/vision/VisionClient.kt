package com.example.emptyactivity.data.remote.vision

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Factory object for creating a configured VisionApi client.
 *
 * This object handles the setup and configuration of the Retrofit client for Google Cloud Vision API.
 * It configures:
 * - API key authentication via query parameter interceptor
 * - JSON serialization/deserialization using Moshi
 * - Base URL for Google Vision API endpoints
 *
 * The created client automatically adds the API key to all requests and handles JSON conversion
 * between Kotlin data classes and the API's JSON format.
 */
object VisionClient {
    /**
     * Creates and configures a VisionApi client instance.
     *
     * This method sets up a complete Retrofit client with:
     * 1. An OkHttp interceptor that automatically adds the API key as a query parameter
     * 2. Moshi JSON converter for automatic serialization of Kotlin data classes
     * 3. Base URL pointing to Google Cloud Vision API
     *
     * @param apiKey The Google Cloud Vision API key. This will be automatically added to all
     *               requests as a query parameter (?key=YOUR_KEY).
     * @return A configured VisionApi instance ready to make OCR requests to Google Vision API.
     */
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
                val req =
                    chain
                        .request()
                        .newBuilder()
                        .url(url)
                        .build()
                chain.proceed(req)
            }

        val http =
            OkHttpClient
                .Builder()
                .addInterceptor(keyInterceptor)
                .build()

        val moshi =
            Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory()) // reflection adapter
                .build()

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://vision.googleapis.com/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(http)
                .build()

        return retrofit.create(VisionApi::class.java)
    }
}
