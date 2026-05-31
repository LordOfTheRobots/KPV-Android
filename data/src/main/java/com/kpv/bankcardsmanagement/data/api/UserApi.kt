package com.kpv.bankcardsmanagement.data.api

import com.kpv.bankcardsmanagement.data.dto.UserProfileDto
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET("/api/v1/user/me")
    suspend fun getUserProfile(): Response<UserProfileDto>
}