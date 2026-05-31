package com.kpv.bankcardsmanagement.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("jwt") val jwt: String,
    @SerialName("refreshToken") val refreshToken: String,
)