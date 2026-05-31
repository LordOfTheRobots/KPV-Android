package com.kpv.bankcardsmanagement.domain.repository

import com.kpv.bankcardsmanagement.domain.core.model.AuthTokens

interface AuthRepository {

    fun isAuthorized(): Boolean
    suspend fun getAccessToken(): String?

    suspend fun login(email: String, password: String): Result<AuthTokens>
    suspend fun register(email: String, password: String): Result<AuthTokens>
    suspend fun logout(): Result<Unit>

    suspend fun refreshToken(): Result<AuthTokens>
    suspend fun saveTokens(tokens: AuthTokens)
    suspend fun clearTokens()
}