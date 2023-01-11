package com.imnidasoftware.animeapp.data.repository

import com.imnidasoftware.animeapp.data.local.AnimeDatabase
import com.imnidasoftware.animeapp.domain.model.Hero
import com.imnidasoftware.animeapp.domain.repository.LocalDataSource

class LocalDataSourceImpl(animeDatabase: AnimeDatabase): LocalDataSource {

    private val heroDao = animeDatabase.heroDao()

    override suspend fun getSelectedHero(heroId: Int): Hero {
        return heroDao.getSelectedHero(heroId)
    }
}