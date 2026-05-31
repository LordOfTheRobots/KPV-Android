package com.kpv.bankcardsmanagement.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardEnteredDto(
    @SerialName("cardId") val cardId: String? = null,
    @SerialName("cardNumber") val cardNumber: String,
    @SerialName("expirationDate") val expiryDate: String,
    @SerialName("cardHolderName") val cardHolderName: String,
    @SerialName("amount") val amount: Double? = null
)