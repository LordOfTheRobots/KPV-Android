package com.kpv.bankcardsmanagement.domain.usecases.auth

import android.util.Patterns
import com.kpv.bankcardsmanagement.domain.core.exceptions.AuthException
import com.kpv.bankcardsmanagement.domain.core.model.AuthTokens
import com.kpv.bankcardsmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthTokens> {
        if (!email.isValidEmail()) {
            return Result.failure(IllegalArgumentException(AuthException.InvalidCredentials))
        }

        return repository.login(email.trim(), password)
    }
}

fun String.isValidEmail(): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(this).matches()