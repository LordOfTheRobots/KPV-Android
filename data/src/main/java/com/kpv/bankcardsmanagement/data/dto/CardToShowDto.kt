package com.kpv.bankcardsmanagement.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CardToShowDto(
    @SerialName("cardId") val cardId: Long,
    @SerialName("cardMask") val cardMask: String,
    @SerialName("expireDate") val expireDate: String,
    @SerialName("balance") val balance: Double,
    @SerialName("cardCondition") val cardCondition: CardConditionDto?,
    @SerialName("owner") val owner: String
)

@Serializable
data class CardConditionDto(
    @SerialName("conditionId") val conditionId: String?,
    @SerialName("conditionName") val conditionName: String?,
    @SerialName("dateOfCondition") val dateOfCondition: String?,
    @SerialName("comment") val comment: String?
)