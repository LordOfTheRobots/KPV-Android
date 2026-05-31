package com.kpv.bankcardsmanagement.domain.usecases.auth
import com.kpv.bankcardsmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val logoutResult = authRepository.logout()

        authRepository.clearTokens()

        return if (logoutResult.isSuccess) {
            Result.success(Unit)
        } else {
            Result.success(Unit)
        }
    }
}