package com.imnidasoftware.animeapp.domain.repository

import com.imnidasoftware.animeapp.domain.model.Hero

interface LocalDataSource {
    suspend fun getSelectedHero(heroId: Int): Hero
}