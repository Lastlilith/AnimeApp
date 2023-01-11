package com.imnidasoftware.animeapp.domain.use_cases.get_selected_hero

import com.imnidasoftware.animeapp.data.repository.Repository
import com.imnidasoftware.animeapp.domain.model.Hero

class GetSelectedHeroUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(heroId: Int): Hero {
        return repository.getSelectedHero(heroId)
    }
}