package com.kpv.bankcardsmanagement.domain.core.model
data class TransactionInput(
    val fromCardId: Long,
    val toCardNumber: String,
    val amount: String,
    val description: String
)