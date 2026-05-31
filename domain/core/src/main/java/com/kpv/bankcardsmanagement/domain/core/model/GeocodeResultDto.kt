package com.kpv.bankcardsmanagement.domain.core.model

data class GeocodeResult(
    val id: String,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val city: String
)