package com.kpv.bankcardsmanagement.data.repository

import com.kpv.bankcardsmanagement.core.database.yandex.yandex.dao.GeocodeCacheDao
import com.kpv.bankcardsmanagement.core.database.yandex.yandex.entity.GeocodeCacheEntity
import com.kpv.bankcardsmanagement.core.maps.api.YandexGeocodeApi
import com.kpv.bankcardsmanagement.core.maps.dto.GeoObject
import com.kpv.bankcardsmanagement.core.maps.dto.toDomainResult
import com.kpv.bankcardsmanagement.domain.core.exceptions.CardException
import com.kpv.bankcardsmanagement.domain.core.model.GeocodeResult
import com.kpv.bankcardsmanagement.domain.repository.MapsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class MapsRepositoryImpl @Inject constructor(
    private val api: YandexGeocodeApi,
    private val cacheDao: GeocodeCacheDao
) : MapsRepository {

    override suspend fun geocode(query: String): Result<List<GeocodeResult>> = withContext(Dispatchers.IO) {
        try {
            val cached = cacheDao.getValidByQuery(query, System.currentTimeMillis())
            if (cached.isNotEmpty()) {
                return@withContext Result.success(cached.map { cacheEntity -> cacheEntity.toDomainResult() })
            }

            val response = api.geocode(query)
            val results: List<GeocodeResult> = response.featureMember.map { feature ->
                feature.geoObject.toDomainResult()
            }

            val entities = results.map { res ->
                GeocodeCacheEntity(
                    id = UUID.randomUUID().toString(),
                    query = query,
                    name = res.name,
                    description = res.description,
                    latitude = res.latitude,
                    longitude = res.longitude,
                    timestamp = System.currentTimeMillis()
                )
            }
            cacheDao.insertAll(entities)
            cacheDao.deleteExpired(System.currentTimeMillis() - 5 * 60 * 1000L)

            Result.success(results)
        } catch (e: Exception) {
            Result.failure(CardException.NetworkError)
        }
    }

    override suspend fun searchNearbyBanks(lat: Double, lon: Double): Result<List<GeocodeResult>> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchBanks("банкомат отделение", "$lon,$lat", "0.05,0.05", "biz")
            val results: List<GeocodeResult> = response.featureMember.map { feature -> feature.geoObject.toDomainResult() }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(CardException.NetworkError)
        }
    }

    override suspend fun clearCache() = withContext(Dispatchers.IO) { cacheDao.clearAll() }
}

fun GeoObject.toDomainResult(): GeocodeResult {
    val parts = point?.position?.trim()?.split("\\s+".toRegex()) ?: emptyList()
    val lon = if (parts.size >= 2) parts[0].toDoubleOrNull() ?: 0.0 else 0.0
    val lat = if (parts.size >= 2) parts[1].toDoubleOrNull() ?: 0.0 else 0.0

    return GeocodeResult(
        id = "yandex_${lon}_${lat}",
        name = name.orEmpty(),
        description = description.orEmpty(),
        latitude = lat,
        longitude = lon,
        city = ""
    )
}

fun GeocodeCacheEntity.toDomainResult(): GeocodeResult {
    return GeocodeResult(
        id = id,
        name = name,
        description = description,
        latitude = latitude,
        longitude = longitude,
        city = ""
    )
}