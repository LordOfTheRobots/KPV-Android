package com.kpv.bankcardsmanagement.domain.core.model

import java.math.BigDecimal

data class CardInput(
    val cardNumber: String,
    val expireDate: String
)

data class Card(
    val cardId: Long,
    val cardMask: String,
    val expireDate: String,
    val balance: Double,
    val condition: CardConditionInfo
)