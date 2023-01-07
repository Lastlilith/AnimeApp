package com.imnidasoftware.animeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.imnidasoftware.animeapp.data.local.dao.HeroDao
import com.imnidasoftware.animeapp.data.local.dao.HeroRemoteKeysDao
import com.imnidasoftware.animeapp.domain.model.Hero
import com.imnidasoftware.animeapp.domain.model.HeroRemoteKeys

@Database(entities = [Hero::class, HeroRemoteKeys::class], version = 1)
@TypeConverters(DatabaseConverter::class)
abstract class AnimeDatabase: RoomDatabase() {

    abstract fun heroDao(): HeroDao
    abstract fun heroRemoteKeysDao(): HeroRemoteKeysDao
}