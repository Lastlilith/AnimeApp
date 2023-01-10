package com.imnidasoftware.animeapp.data.paging_source

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.imnidasoftware.animeapp.data.local.AnimeDatabase
import com.imnidasoftware.animeapp.data.remote.AnimeApi
import com.imnidasoftware.animeapp.domain.model.Hero
import com.imnidasoftware.animeapp.domain.model.HeroRemoteKeys
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@ExperimentalPagingApi
class HeroRemoteMediator @Inject constructor(
    private val animeApi: AnimeApi,
    private val animeDatabase: AnimeDatabase
) : RemoteMediator<Int, Hero>() {

    private val heroDao = animeDatabase.heroDao()
    private val heroRemoteKeysDao = animeDatabase.heroRemoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        val currentTime = System.currentTimeMillis()
        val lastUpdated = heroRemoteKeysDao.getRemoteKeys(1)?.lastUpdated ?: 0L
        val cacheTimeout = 1440
        Log.d("RemoteMediator", "Current Time: ${parseMillis(currentTime)}")
        Log.d("RemoteMediator", "Last Updated Time: ${parseMillis(lastUpdated)}")

        val differenceInMinutes = (currentTime - lastUpdated) / 1000 / 60
        return if (differenceInMinutes.toInt() <= cacheTimeout) {
            Log.d("RemoteMediator", "Up to date!")
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            Log.d("RemoteMediator", "Refresh")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Hero>): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextPage
                }
            }

            val response = animeApi.getAllHeroes(page = page)
            if (response.heroes.isNotEmpty()) {
                animeDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        heroDao.deleteAllHeroes()
                        heroRemoteKeysDao.deleteAllRemoteKeys()
                    }
                    val prevPage = response.prevPage
                    val nextPage = response.nextPage
                    val keys = response.heroes.map { hero ->
                        HeroRemoteKeys(
                            id = hero.id,
                            prevPage = prevPage,
                            nextPage = nextPage,
                            lastUpdated = response.lastUpdated
                        )
                    }
                    heroRemoteKeysDao.addAllRemoteKeys(keys)
                    heroDao.addHeroes(heroes = response.heroes)
                }
            }
            MediatorResult.Success(endOfPaginationReached = response.nextPage == null)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Hero>): HeroRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                heroRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Hero>): HeroRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { hero ->
                heroRemoteKeysDao.getRemoteKeys(id = hero.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Hero>): HeroRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { hero ->
                heroRemoteKeysDao.getRemoteKeys(id = hero.id)
            }
    }

    private fun parseMillis(millis: Long): String {
        val date = Date(millis)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.ROOT)
        return format.format(date)
    }
}