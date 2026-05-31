package com.kpv.bankcardsmanagement.core.maps.dto

import com.kpv.bankcardsmanagement.domain.core.model.GeocodeResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoObjectCollection(
    @SerialName("featureMember") val featureMember: List<FeatureMember> = emptyList()
)

@Serializable
data class FeatureMember(
    @SerialName("GeoObject") val geoObject: GeoObject
)

@Serializable
data class GeoObject(
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("Point") val point: Point? = null
)

@Serializable
data class Point(
    @SerialName("pos") val position: String? = null
)

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