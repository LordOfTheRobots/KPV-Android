package com.kpv.bankcardsmanagement.domain.core.exceptions

sealed class CardException(message: String) : Exception(message) {
    object InvalidNumber : CardException("Invalid card number")
    object InvalidDate : CardException("Invalid expire date")
    object CardNotFound : CardException("Card not found")
    object NetworkError : CardException("Network or server error")
    object Unknown : CardException("Unknown error")
}