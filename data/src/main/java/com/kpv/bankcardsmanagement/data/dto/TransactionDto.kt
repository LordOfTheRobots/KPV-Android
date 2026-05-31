package com.kpv.bankcardsmanagement.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionViewDto(
    @SerialName("transactionId") val transactionId: String,
    @SerialName("amount") val amount: String,
    @SerialName("transactionDate") val transactionDate: String,
    @SerialName("description") val description: String,
    @SerialName("mainCardId") val mainCardId: Long,
    @SerialName("mainCardNumber") val mainCardNumber: String,
    @SerialName("secondaryCard") val secondaryCard: String,
    @SerialName("userEmail") val userEmail: String
)

@Serializable
data class MakeTransactionRequest(
    @SerialName("cardId") val cardId: Long,
    @SerialName("description") val description: String,
    @SerialName("amount") val amount: String,
    @SerialName("cardToTransact") val cardToTransact: String
)

@Serializable
data class CardSpendingByDaysDto(
    @SerialName("cardId") val cardId: Long,
    @SerialName("startDate") val startDate: String,
    @SerialName("endDate") val endDate: String,
    @SerialName("days") val days: List<DailySpendingDto>
)

@Serializable
data class DailySpendingDto(
    @SerialName("date") val date: String,
    @SerialName("amount") val amount: String
)