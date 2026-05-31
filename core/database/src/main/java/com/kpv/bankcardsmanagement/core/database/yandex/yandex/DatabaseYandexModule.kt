package com.kpv.bankcardsmanagement.core.database.yandex.yandex

import android.app.Application
import android.content.Context
import com.kpv.bankcardsmanagement.core.database.yandex.yandex.dao.GeocodeCacheDao
import com.kpv.bankcardsmanagement.core.database.yandex.yandex.database.GeocodeDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
object DatabaseYandexModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): GeocodeDatabase {
        return GeocodeDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideCacheDao(database: GeocodeDatabase): GeocodeCacheDao {
        return database.cacheDao()
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
}