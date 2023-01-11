package com.imnidasoftware.animeapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.imnidasoftware.animeapp.data.local.AnimeDatabase
import com.imnidasoftware.animeapp.data.paging_source.HeroRemoteMediator
import com.imnidasoftware.animeapp.data.paging_source.SearchHeroesSource
import com.imnidasoftware.animeapp.data.remote.AnimeApi
import com.imnidasoftware.animeapp.domain.model.Hero
import com.imnidasoftware.animeapp.domain.repository.RemoteDataSource
import com.imnidasoftware.animeapp.util.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class RemoteDataSourceImpl(
    private val animeApi: AnimeApi,
    private val animeDatabase: AnimeDatabase
): RemoteDataSource {

    private val heroDao = animeDatabase.heroDao()


    override fun getAllHeroes(): Flow<PagingData<Hero>> {
        val pagingSourceFactory = {heroDao.getAllHeroes()}
        return Pager(
            config = PagingConfig(pageSize =  ITEMS_PER_PAGE),
            remoteMediator = HeroRemoteMediator(
                animeApi = animeApi,
                animeDatabase = animeDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun searchHeroes(query: String): Flow<PagingData<Hero>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                SearchHeroesSource(
                    animeApi = animeApi,
                    query = query
                )
            }
        ).flow
    }

}