package com.kpv.bankcardsmanagement.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardCondition(
    @SerialName("conditionName") val conditionName: String,

    @SerialName("dateOfCondition") val dateOfCondition: String, //Timestamp

    @SerialName("isUsable") private val isUsable: Boolean) {

}