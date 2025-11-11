package com.example.emptyactivity.domain.usecase.menu
import com.example.emptyactivity.data.remote.gemini.GeminiClient
import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.util.Result
import javax.inject.Inject
class AnalyzeMenuUseCase @Inject constructor(
    private val geminiClient: GeminiClient,
    private val userProfileRepository: UserProfileRepository
){
    
}