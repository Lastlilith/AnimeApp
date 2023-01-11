package com.imnidasoftware.animeapp.domain.use_cases.search_heroes

import androidx.paging.PagingData
import com.imnidasoftware.animeapp.data.repository.Repository
import com.imnidasoftware.animeapp.domain.model.Hero
import kotlinx.coroutines.flow.Flow

class SearchHeroesUseCase(
    private val repository: Repository
) {
    operator fun invoke(query: String): Flow<PagingData<Hero>>  {
        return repository.searchHeroes(query)
    }
}