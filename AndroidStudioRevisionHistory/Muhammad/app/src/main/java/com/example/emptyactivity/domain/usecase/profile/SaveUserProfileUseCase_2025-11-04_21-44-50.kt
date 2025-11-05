package com.example.emptyactivity.domain.usecase.profile

import javax.inject.Inject

class  SaveUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository, 
    private val authRepository: AuthRepository
){
    
}