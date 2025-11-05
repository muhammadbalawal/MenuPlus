package com.example.emptyactivity.domain.usecase.profile

import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.domain.model.Language
import com.example.emptyactivity.util.Result
import javax.inject.Inject

class GetAllLanguagesUseCase
    @Inject
    constructor(
        private val userProfileRepository: UserProfileRepository,
    ) {
        suspend operator fun invoke(): Result<List<Language>> = userProfileRepository.getAllLanguages()
    }
