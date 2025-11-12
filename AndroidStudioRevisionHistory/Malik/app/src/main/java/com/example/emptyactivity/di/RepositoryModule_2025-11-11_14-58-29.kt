package com.example.emptyactivity.di

import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.data.repository.auth.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for binding repository implementations to their interfaces.
 *
 * This module uses Dagger's @Binds annotation to bind concrete repository implementations
 * to their interface abstractions. This follows the dependency inversion principle by
 * allowing the domain layer to depend on abstractions rather than concrete implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    /**
     * Binds the AuthRepositoryImpl to the AuthRepository interface.
     *
     * This binding makes AuthRepositoryImpl available for injection wherever AuthRepository
     * is requested. The implementation is provided as a singleton to ensure consistent state
     * management across the application.
     *
     * @param impl The concrete implementation of AuthRepository.
     * @return The AuthRepository interface bound to AuthRepositoryImpl.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository
}
