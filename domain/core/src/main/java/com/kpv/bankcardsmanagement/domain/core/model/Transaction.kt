package com.kpv.bankcardsmanagement.domain.core.model

data class Transaction(
    val id: String,
    val amount: Double,
    val date: String,
    val description: String,
    val type: TransactionType
)

data class DailySpending(val date: String, val amount: Double)