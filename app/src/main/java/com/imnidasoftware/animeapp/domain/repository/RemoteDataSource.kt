package com.imnidasoftware.animeapp.domain.repository

import androidx.paging.PagingData
import com.imnidasoftware.animeapp.domain.model.Hero
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getAllHeroes(): Flow<PagingData<Hero>>
    fun searchHeroes(query: String): Flow<PagingData<Hero>>
}