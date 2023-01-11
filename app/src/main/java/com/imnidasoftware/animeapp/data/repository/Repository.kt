package com.imnidasoftware.animeapp.data.repository

import androidx.paging.PagingData
import com.imnidasoftware.animeapp.domain.model.Hero
import com.imnidasoftware.animeapp.domain.repository.DataStoreOperations
import com.imnidasoftware.animeapp.domain.repository.LocalDataSource
import com.imnidasoftware.animeapp.domain.repository.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val dataStore: DataStoreOperations
) {

    fun getAllHeroes(): Flow<PagingData<Hero>> {
        return remote.getAllHeroes()
    }
    fun searchHeroes(query: String): Flow<PagingData<Hero>> {
        return remote.searchHeroes(query)
    }

    suspend fun getSelectedHero(heroId: Int): Hero {
        return local.getSelectedHero(heroId)
    }

    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.saveOnBoardingState(completed)
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return dataStore.readOnBoardingState()
    }
}