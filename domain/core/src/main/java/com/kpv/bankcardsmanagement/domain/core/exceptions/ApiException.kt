package com.kpv.bankcardsmanagement.domain.core.exceptions

class ApiException(val code:Int? = 0, message: String) : Exception(message) {
}