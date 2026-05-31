package com.kpv.bankcardsmanagement.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthDto(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)