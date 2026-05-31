package com.kpv.bankcardsmanagement.domain.usecases.auth

import com.kpv.bankcardsmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): String? = authRepository.getAccessToken()
}