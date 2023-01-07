package com.imnidasoftware.animeapp.di

import androidx.paging.ExperimentalPagingApi
import com.imnidasoftware.animeapp.data.local.AnimeDatabase
import com.imnidasoftware.animeapp.data.remote.AnimeApi
import com.imnidasoftware.animeapp.data.repository.RemoteDataSourceImpl
import com.imnidasoftware.animeapp.domain.repository.RemoteDataSource
import com.imnidasoftware.animeapp.util.Constants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@ExperimentalPagingApi
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.MINUTES)
            .connectTimeout(15, TimeUnit.MINUTES)
            .build()
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        val contentType = MediaType.get("application/json")
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideAnimeApi(retrofit: Retrofit): AnimeApi {
        return retrofit.create(AnimeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        animeApi: AnimeApi,
        animeDatabase: AnimeDatabase
    ): RemoteDataSource {
        return RemoteDataSourceImpl(
            animeApi = animeApi,
            animeDatabase = animeDatabase
        )
    }
}