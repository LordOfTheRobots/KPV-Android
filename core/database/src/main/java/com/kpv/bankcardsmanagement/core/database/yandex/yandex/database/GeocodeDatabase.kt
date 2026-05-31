package com.kpv.bankcardsmanagement.core.database.yandex.yandex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kpv.bankcardsmanagement.core.database.yandex.yandex.dao.GeocodeCacheDao
import com.kpv.bankcardsmanagement.core.database.yandex.yandex.entity.GeocodeCacheEntity

@Database(entities = [GeocodeCacheEntity::class], version = 1)
abstract class GeocodeDatabase : RoomDatabase() {
    abstract fun cacheDao(): GeocodeCacheDao

    companion object {
        @Volatile private var instance: GeocodeDatabase? = null

        fun getInstance(context: Context): GeocodeDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    GeocodeDatabase::class.java,
                    "mapkit_cache_db"
                ).build().also { instance = it }
            }
        }
    }
}