package com.kpv.bankcardsmanagement.core.database.yandex.yandex.dao

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.kpv.bankcardsmanagement.core.database.yandex.yandex.entity.GeocodeCacheEntity

const val GEOCODE_CACHE = "geocode_cache"

@Dao
interface
GeocodeCacheDao {
    companion object {
        const val GEOCODE_CACHE = "geocode_cache"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(caches: List<GeocodeCacheEntity>)

    @Query("SELECT * FROM $GEOCODE_CACHE WHERE `query` = :query ORDER BY timestamp DESC")
    suspend fun getByQuery(query: String): List<GeocodeCacheEntity>

    @Query("SELECT * FROM $GEOCODE_CACHE WHERE `query` = :query AND timestamp > :currentTime")
    suspend fun getValidByQuery(query: String, currentTime: Long): List<GeocodeCacheEntity>

    @Query("DELETE FROM $GEOCODE_CACHE WHERE `query` = :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM $GEOCODE_CACHE WHERE `timestamp` < :expireTime")
    suspend fun deleteExpired(expireTime: Long)

    @Query("DELETE FROM $GEOCODE_CACHE")
    suspend fun clearAll()
}