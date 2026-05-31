package com.kpv.bankcardsmanagement.domain.repository

import com.kpv.bankcardsmanagement.domain.core.model.Transaction
import com.kpv.bankcardsmanagement.domain.core.model.TransactionInput

interface TransactionsRepository {
    suspend fun getUserTransactions(): Result<List<Transaction>>
    suspend fun makeTransaction(input: TransactionInput): Result<Unit>
    suspend fun getCardTransactions(cardId: Long): Result<List<Transaction>>
}