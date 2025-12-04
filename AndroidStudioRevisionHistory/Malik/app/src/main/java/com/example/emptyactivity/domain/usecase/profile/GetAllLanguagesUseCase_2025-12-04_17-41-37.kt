package com.example.emptyactivity.domain.usecase.profile

import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.domain.model.Language
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for retrieving all available languages from the database.
 *
 * This use case fetches the complete list of languages that users can select as their
 * preferred language. Languages are used for menu translation and UI personalization.
 *
 * The use case is typically called during onboarding or profile editing to populate
 * the language selection dropdown.
 *
 * @param userProfileRepository The repository for accessing language data. Injected via Hilt.
 */
class GetAllLanguagesUseCase
    @Inject
    constructor(
        private val userProfileRepository: UserProfileRepository,
    ) {
        /**
         * Retrieves all available languages from the database.
         *
         * @return Result containing a list of Language objects on success, or an error message
         *         if the operation fails. Returns an empty list if no languages are available.
         */
        suspend operator fun invoke(): Result<List<Language>> = userProfileRepository.getAllLanguages()
    }
