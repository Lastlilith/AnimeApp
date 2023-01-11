package com.imnidasoftware.animeapp.di

import android.content.Context
import androidx.room.Room
import com.imnidasoftware.animeapp.data.local.AnimeDatabase
import com.imnidasoftware.animeapp.data.repository.LocalDataSourceImpl
import com.imnidasoftware.animeapp.domain.repository.LocalDataSource
import com.imnidasoftware.animeapp.util.Constants.ANIME_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AnimeDatabase {
        return Room.databaseBuilder(
            context,
            AnimeDatabase::class.java,
            ANIME_DATABASE,
        ).build()
    }

    @Provides
    @Singleton
    fun probideLocalDataSource(
        database: AnimeDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(
            animeDatabase = database
        )
    }

}