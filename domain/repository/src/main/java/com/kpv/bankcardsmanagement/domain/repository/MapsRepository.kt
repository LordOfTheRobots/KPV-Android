package com.kpv.bankcardsmanagement.domain.repository

import com.kpv.bankcardsmanagement.domain.core.model.GeocodeResult


interface MapsRepository {
    suspend fun geocode(query: String): Result<List<GeocodeResult>>
    suspend fun searchNearbyBanks(lat: Double, lon: Double): Result<List<GeocodeResult>>
    suspend fun clearCache()
}