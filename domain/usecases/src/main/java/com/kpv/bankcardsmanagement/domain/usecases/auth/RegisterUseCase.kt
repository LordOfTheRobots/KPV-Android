package com.kpv.bankcardsmanagement.domain.usecases.auth

import com.kpv.bankcardsmanagement.domain.core.exceptions.AuthException
import com.kpv.bankcardsmanagement.domain.core.model.AuthTokens
import com.kpv.bankcardsmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<AuthTokens> {
        require(email.isValidEmail()) { AuthException.InvalidCredentials }
        require(password.length >= 8) { AuthException.ShortPassword }
        require(password.any { !it.isDigit() }) { AuthException.NoDigits }
        require(password.any { !it.isLetterOrDigit() }) { AuthException.NoSpecialCharacters }
        require(password.any{!it.isLowerCase()}){ AuthException.NoLowerCaseLetters }
        require(password.any({!it.isUpperCase()})){ AuthException.NoUpperCaseLetters }

        return repository.register(email.trim(), password)
    }
}