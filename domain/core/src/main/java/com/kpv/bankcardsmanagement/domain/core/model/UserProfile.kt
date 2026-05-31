package com.kpv.bankcardsmanagement.domain.core.model
data class UserProfile(
    val email: String,
    val telephoneNumber: String?,
    val telegramId: String?,
    val roleName: String
)