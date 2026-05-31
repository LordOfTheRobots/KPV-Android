package com.kpv.bankcardsmanagement.domain.usecases.transactions

import com.kpv.bankcardsmanagement.domain.repository.TransactionsRepository
import javax.inject.Inject

class GetUserTransactionsUseCase @Inject constructor(private val repo: TransactionsRepository) {
    suspend operator fun invoke() = repo.getUserTransactions()
}