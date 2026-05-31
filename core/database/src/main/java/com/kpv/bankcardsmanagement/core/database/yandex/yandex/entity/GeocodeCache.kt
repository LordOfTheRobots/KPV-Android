package com.kpv.bankcardsmanagement.core.database.yandex.yandex.entity

import androidx.room.Entity;
import androidx.room.Index
import androidx.room.PrimaryKey;
import com.kpv.bankcardsmanagement.domain.core.model.GeocodeResult

@Entity(
    tableName = "geocode_cache",
    indices = [Index("query")]
)
data class GeocodeCacheEntity(
    @PrimaryKey val id: String,
    val query: String,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val ttlMillis: Long = 5 * 60 * 1000L
) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() - timestamp > ttlMillis

    fun toDomain(): GeocodeResult = GeocodeResult(
        id = id,
        name = name,
        description = description,
        latitude = latitude,
        longitude = longitude,
        city = ""
    )
}