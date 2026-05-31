package com.kpv.bankcardsmanagement.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardEnteredRequest(
    @SerialName("cardNumber") val cardNumber: String,
    @SerialName("expirationDate") val expireDate: String
)