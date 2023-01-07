package com.imnidasoftware.animeapp.domain.use_cases.get_all_heroes

import androidx.paging.PagingData
import com.imnidasoftware.animeapp.data.repository.Repository
import com.imnidasoftware.animeapp.domain.model.Hero
import kotlinx.coroutines.flow.Flow

class GetAllHeroesUseCase(
    private val repository: Repository
) {
    operator fun invoke(): Flow<PagingData<Hero>> {
        return repository.getAllHeroes()
    }
}