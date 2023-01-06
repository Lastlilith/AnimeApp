package com.imnidasoftware.animeapp.domain.use_cases.save_onboarding

import com.imnidasoftware.animeapp.data.repository.Repository

class SaveOnBoardingUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(completed: Boolean) {
        repository.saveOnBoardingState(completed)
    }
}