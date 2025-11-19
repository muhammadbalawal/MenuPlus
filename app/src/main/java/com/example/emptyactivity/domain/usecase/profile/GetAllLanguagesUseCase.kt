package com.example.emptyactivity.domain.usecase.profile

import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.domain.model.Language
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for retrieving all available languages.
 *
 * This use case fetches the complete list of languages from the repository that users
 * can select as their preferred language for menu translation. It delegates to the
 * UserProfileRepository to retrieve the language data.
 *
 * @param userProfileRepository The repository used to fetch language data.
 */
class GetAllLanguagesUseCase
    @Inject
    constructor(
        private val userProfileRepository: UserProfileRepository,
    ) {
        suspend operator fun invoke(): Result<List<Language>> = userProfileRepository.getAllLanguages()
    }
