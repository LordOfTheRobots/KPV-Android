package com.kpv.bankcardsmanagement.data.api

import com.kpv.bankcardsmanagement.data.dto.AuthResponse
import com.kpv.bankcardsmanagement.data.dto.UserAuthDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/sign-in")
    suspend fun signIn(
        @Body userAuthDto: UserAuthDto
    ): Response<AuthResponse>

    @POST("api/v1/auth/sign-up")
    suspend fun signUp(
        @Body userAuthDto: UserAuthDto
    ): Response<AuthResponse>

    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(): Response<AuthResponse>
}
