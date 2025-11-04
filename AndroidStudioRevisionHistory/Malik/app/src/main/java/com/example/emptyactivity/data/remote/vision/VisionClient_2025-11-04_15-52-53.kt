package com.example.emptyactivity.data.remote.vision

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object VisionClient {
    /**
     * Factory that builds a VisionApi using the provided API key.
     * NO BuildConfig reference here; the key is passed in by DI.
     */
    fun create(apiKey: String): VisionApi {
        val keyInterceptor = Interceptor { chain ->
            val url = chain.request().url.newBuilder()
                .addQueryParameter("key", apiKey)
                .build()
            val req = chain.request().newBuilder().url(url).build()
            chain.proceed(req)
        }

        val http = OkHttpClient.Builder()
            .addInterceptor(keyInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://vision.googleapis.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(http)
            .build()

        return retrofit.create(VisionApi::class.java)
    }
}
