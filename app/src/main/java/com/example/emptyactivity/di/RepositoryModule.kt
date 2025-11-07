package com.example.emptyactivity.di

import com.example.emptyactivity.data.remote.vision.VisionApi
import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.data.repository.auth.AuthRepositoryImpl
import com.example.emptyactivity.data.repository.ocr.OcrRepository
import com.example.emptyactivity.data.repository.ocr.OcrRepositoryImpl
import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.data.repository.profile.UserProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // Auth repository binding
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository

    // User profile repository binding
    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(
        impl: UserProfileRepositoryImpl,
    ): UserProfileRepository

    companion object {
        // OCR repository provider (needs VisionApi dependency)
        @Provides
        @Singleton
        fun provideOcrRepository(api: VisionApi): OcrRepository =
            OcrRepositoryImpl(api)
    }
}
