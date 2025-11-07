package com.example.emptyactivity.data.remote.vision

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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
