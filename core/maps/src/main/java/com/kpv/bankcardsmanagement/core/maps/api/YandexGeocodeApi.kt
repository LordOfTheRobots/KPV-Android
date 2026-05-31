package com.kpv.bankcardsmanagement.core.maps.api

import com.kpv.bankcardsmanagement.core.maps.dto.GeoObjectCollection
import retrofit2.http.GET
import retrofit2.http.Query

interface YandexGeocodeApi {
    @GET("geocode")
    suspend fun geocode(
        @Query("geocode") address: String,
        @Query("format") format: String = "json",
        @Query("results") results: Int = 5
    ): GeoObjectCollection

    @GET("search")
    suspend fun searchBanks(
        @Query("text") query: String,
        @Query("ll") lonLat: String,
        @Query("spn") span: String = "0.05,0.05",
        @Query("type") type: String = "bank",
        @Query("results") results: Int = 20
    ): GeoObjectCollection
}