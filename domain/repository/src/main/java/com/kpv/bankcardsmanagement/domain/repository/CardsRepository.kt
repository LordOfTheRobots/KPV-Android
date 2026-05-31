package com.kpv.bankcardsmanagement.domain.repository

import com.kpv.bankcardsmanagement.domain.core.model.Card
import com.kpv.bankcardsmanagement.domain.core.model.CardInput
import com.kpv.bankcardsmanagement.domain.core.model.DailySpending
import com.kpv.bankcardsmanagement.domain.core.model.Transaction

interface CardsRepository {
    suspend fun getUserCards(): Result<List<Card>>
    suspend fun getCardTransactions(cardId: Long): Result<List<Transaction>>
    suspend fun getWeeklySpending(cardId: Long): Result<List<DailySpending>>
    suspend fun blockCard(cardId: Long): Result<Unit>
    suspend fun addCard(input: CardInput): Result<Unit>
}