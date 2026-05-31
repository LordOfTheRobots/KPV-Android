package com.kpv.bankcardsmanagement.domain.usecases.transactions

import com.kpv.bankcardsmanagement.domain.core.model.Transaction
import com.kpv.bankcardsmanagement.domain.repository.TransactionsRepository
import javax.inject.Inject

class GetCardTransactionsUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    suspend operator fun invoke(cardId: Long): Result<List<Transaction>> {
        return repository.getCardTransactions(cardId)
    }
}