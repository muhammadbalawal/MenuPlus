package com.example.emptyactivity.di

import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.data.repository.auth.AuthRepositoryImpl
import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.data.repository.profile.UserProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(
        impl: UserProfileRepositoryImpl
    ): UserProfileRepository
}