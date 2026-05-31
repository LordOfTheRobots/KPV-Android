package com.kpv.bankcardsmanagement.domain.core.exceptions

import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class AuthException() : Exception() {
    object InvalidCredentials : AuthException()
    object UserAlreadyExists : AuthException()
    object ShortPassword: AuthException()
    object NoSpecialCharacters: AuthException()
    object NoUpperCaseLetters: AuthException()
    object NoLowerCaseLetters: AuthException()
    object NoDigits: AuthException()
    object NetworkError : AuthException()
    object ServerError : AuthException()
    object Unknown : AuthException()

    companion object {
        fun fromThrowable(t: Throwable): AuthException = when (t) {
            is AuthException -> t
            is UnknownHostException, is SocketTimeoutException -> NetworkError
            is ApiException -> when (t.code) {
                401, 403 -> InvalidCredentials
                409 -> UserAlreadyExists
                in 500..599 -> ServerError
                else -> Unknown
            }
            else -> Unknown
        }
    }
}