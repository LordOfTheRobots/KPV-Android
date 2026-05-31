package com.kpv.bankcardsmanagement.data.repository

import com.kpv.bankcardsmanagement.data.api.UserApi
import com.kpv.bankcardsmanagement.data.dto.UserProfileDto
import com.kpv.bankcardsmanagement.domain.core.exceptions.CardException
import com.kpv.bankcardsmanagement.domain.core.model.UserProfile
import com.kpv.bankcardsmanagement.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val api: UserApi) : UserRepository {

    override suspend fun getUserProfile(): Result<UserProfile> = try {
        val res = api.getUserProfile()
        if (res.isSuccessful && res.body() != null) {
            Result.success(res.body()!!.toDomain())
        } else {
            Result.failure(CardException.NetworkError)
        }
    } catch (e: Exception) {
        Result.failure(CardException.NetworkError)
    }

    private fun UserProfileDto.toDomain(): UserProfile = UserProfile(
        email = email,
        telephoneNumber = telephoneNumber,
        telegramId = telegramId,
        roleName = roleName
    )
}