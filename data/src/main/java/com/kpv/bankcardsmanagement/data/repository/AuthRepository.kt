package com.kpv.bankcardsmanagement.data.repository

import com.kpv.bankcardsmanagement.core.common.page.mapFailure
import com.kpv.bankcardsmanagement.core.network.util.AuthTokenProvider
import com.kpv.bankcardsmanagement.data.api.AuthApi
import com.kpv.bankcardsmanagement.data.dto.AuthResponse
import com.kpv.bankcardsmanagement.data.dto.UserAuthDto
import com.kpv.bankcardsmanagement.domain.core.exceptions.ApiException
import com.kpv.bankcardsmanagement.domain.core.exceptions.AuthException
import com.kpv.bankcardsmanagement.domain.core.model.AuthTokens
import com.kpv.bankcardsmanagement.domain.repository.AuthRepository
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val preferences: AuthTokenProvider
) : AuthRepository {

    override fun isAuthorized(): Boolean =
        preferences.getJwtToken() != null

    override suspend fun getAccessToken(): String? =
        preferences.getJwtToken()

    override suspend fun login(email: String, password: String): Result<AuthTokens> =
        runCatching {
            val response = api.signIn(UserAuthDto(email, password))
            if (!response.isSuccessful) throw ApiException(response.code(), response.message())

            val body = response.body() ?: throw ApiException(message = "Empty response body")

            val tokens = body.toDomain()
            preferences.saveTokens(tokens.accessToken, tokens.refreshToken)
            tokens
        }.mapFailure { mapToAuthException(it) }

    override suspend fun register(email: String, password: String): Result<AuthTokens> =
        runCatching {
            val response = api.signUp(UserAuthDto(email, password))
            if (!response.isSuccessful) throw ApiException(response.code(), response.message())

            val body = response.body() ?: throw ApiException(message = "Empty response body")

            val tokens = body.toDomain()
            preferences.saveTokens(tokens.accessToken, tokens.refreshToken)
            tokens
        }.mapFailure { mapToAuthException(it) }

    override suspend fun logout(): Result<Unit> = runCatching {
        preferences.clearAuthData()
    }

    override suspend fun refreshToken(): Result<AuthTokens> = runCatching {
        val refresh = preferences.getRefreshToken()
            ?: throw ApiException(message = "No refresh token available")

        val response = api.refreshToken()
        if (!response.isSuccessful) throw ApiException(response.code(), response.message())

        val body = response.body() ?: throw ApiException(message = "Empty response body")

        val tokens = body.toDomain()
        preferences.saveTokens(tokens.accessToken, tokens.refreshToken)
        tokens
    }.mapFailure { mapToAuthException(it) }

    override suspend fun saveTokens(tokens: AuthTokens) = preferences.saveTokens(
        tokens.accessToken, tokens.refreshToken
    )

    override suspend fun clearTokens() = preferences.clearAuthData()

    private fun mapToAuthException(throwable: Throwable): AuthException = when (throwable) {
        is ApiException -> when (throwable.code) {
            401, 403 -> AuthException.InvalidCredentials
            409 -> AuthException.UserAlreadyExists
            in 500..599 -> AuthException.ServerError
            else -> AuthException.NetworkError
        }
        is UnknownHostException, is SocketTimeoutException ->
            AuthException.NetworkError
        else -> AuthException.Unknown
    }
}

private fun AuthResponse.toDomain() = AuthTokens(
    accessToken = jwt,
    refreshToken = refreshToken
)