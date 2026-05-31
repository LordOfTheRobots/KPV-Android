package com.kpv.bankcardsmanagement.domain.usecases.transactions

import com.kpv.bankcardsmanagement.domain.core.model.TransactionInput
import com.kpv.bankcardsmanagement.domain.repository.TransactionsRepository
import javax.inject.Inject

class MakeTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    suspend operator fun invoke(input: TransactionInput): Result<Unit> {
        val cleanAmount = input.amount.replace(",", ".")
        if (cleanAmount.toDoubleOrNull() == null || cleanAmount.toDouble() <= 0) {
            return Result.failure(IllegalArgumentException("InvalidAmount"))
        }
        return repository.makeTransaction(input.copy(amount = cleanAmount))
    }
}