package com.kpv.bankcardsmanagement.domain.core.model

data class CardConditionInfo(
    val status: CardStatus,
    val comment: String?,
    val dateOfCondition: String?
)