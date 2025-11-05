package com.example.emptyactivity.di

import com.example.emptyactivity.data.remote.vision.VisionApi
import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.data.repository.auth.AuthRepositoryImpl
import com.example.emptyactivity.data.repository.ocr.OcrRepository
import com.example.emptyactivity.data.repository.ocr.OcrRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Existing Auth binding (Binds = abstract)
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository

    companion object {
        //New OCR provider (Provides = concrete)
        @Provides
        @Singleton
        fun provideOcrRepository(api: VisionApi): OcrRepository =
            OcrRepositoryImpl(api)
    }
}
