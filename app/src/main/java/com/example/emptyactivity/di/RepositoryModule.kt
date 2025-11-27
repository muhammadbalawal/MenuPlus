package com.example.emptyactivity.di

import com.example.emptyactivity.data.remote.vision.VisionApi
import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.data.repository.auth.AuthRepositoryImpl
import com.example.emptyactivity.data.repository.ocr.OcrRepository
import com.example.emptyactivity.data.repository.ocr.OcrRepositoryImpl
import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.data.repository.profile.UserProfileRepositoryImpl
import com.example.emptyactivity.data.repository.menu.MenuRepository
import com.example.emptyactivity.data.repository.menu.MenuRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing repository implementations.
 *
 * This module binds repository interfaces to their implementations using dependency injection.
 * It uses two different approaches:
 * - @Binds for simple interface-to-implementation bindings (Auth, UserProfile)
 * - @Provides for repositories that need additional dependencies (OCR needs VisionApi)
 *
 * All repositories are provided as singletons to ensure consistent data access throughout
 * the application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    /**
     * Binds AuthRepository interface to AuthRepositoryImpl implementation.
     *
     * This binding allows the app to use AuthRepository interface while Hilt automatically
     * provides the concrete implementation. This enables easy testing by allowing mock
     * implementations to be swapped in.
     *
     * @param impl The concrete implementation of AuthRepository. Injected automatically by Hilt.
     * @return AuthRepository interface instance (actually the implementation).
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository

    /**
     * Binds UserProfileRepository interface to UserProfileRepositoryImpl implementation.
     *
     * This binding provides access to user profile data (dietary preferences, allergies,
     * language preferences) stored in Supabase.
     *
     * @param impl The concrete implementation of UserProfileRepository. Injected automatically by Hilt.
     * @return UserProfileRepository interface instance (actually the implementation).
     */
    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(
        impl: UserProfileRepositoryImpl,
    ): UserProfileRepository

    /**
     * Binds MenuRepository interface to MenuRepositoryImpl implementation.
     *
     * This binding provides access to saved menu data stored in Supabase.
     *
     * @param impl The concrete implementation of MenuRepository. Injected automatically by Hilt.
     * @return MenuRepository interface instance (actually the implementation).
     */
    @Binds
    @Singleton
    abstract fun bindMenuRepository(
        impl: MenuRepositoryImpl,
    ): MenuRepository

    companion object {
        /**
         * Provides OCR repository implementation with VisionApi dependency.
         *
         * This uses @Provides instead of @Binds because OcrRepositoryImpl requires
         * a VisionApi dependency that needs to be injected. The VisionApi is provided
         * by NetworkModule and automatically injected here.
         *
         * @param api The VisionApi instance for making OCR requests. Injected automatically by Hilt.
         * @return OcrRepository implementation configured with Vision API client.
         */
        @Provides
        @Singleton
        fun provideOcrRepository(api: VisionApi): OcrRepository =
            OcrRepositoryImpl(api)
    }
}
