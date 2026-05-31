package com.kpv.bankcardsmanagement.domain.usecases.user

import com.kpv.bankcardsmanagement.domain.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke() = repo.getUserProfile()
}