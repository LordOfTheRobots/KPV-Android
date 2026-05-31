package com.kpv.bankcardsmanagement.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    @SerialName("userId") val userId: String,
    @SerialName("email") val email: String,
    @SerialName("telephoneNumber") val telephoneNumber: String?,
    @SerialName("telegramId") val telegramId: String?,
    @SerialName("roleName") val roleName: String
)