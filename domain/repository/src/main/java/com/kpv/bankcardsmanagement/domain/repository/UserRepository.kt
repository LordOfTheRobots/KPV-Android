package com.kpv.bankcardsmanagement.domain.repository

import com.kpv.bankcardsmanagement.domain.core.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(): Result<UserProfile>
}