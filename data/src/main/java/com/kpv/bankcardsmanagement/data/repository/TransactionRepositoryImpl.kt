package com.kpv.bankcardsmanagement.data.repository
import com.kpv.bankcardsmanagement.data.api.TransactionsApi
import com.kpv.bankcardsmanagement.data.dto.MakeTransactionRequest
import com.kpv.bankcardsmanagement.data.dto.TransactionViewDto
import com.kpv.bankcardsmanagement.domain.core.exceptions.CardException
import com.kpv.bankcardsmanagement.domain.core.model.Transaction
import com.kpv.bankcardsmanagement.domain.core.model.TransactionInput
import com.kpv.bankcardsmanagement.domain.core.model.TransactionType
import com.kpv.bankcardsmanagement.domain.repository.TransactionsRepository
import javax.inject.Inject
import kotlin.math.absoluteValue

class TransactionsRepositoryImpl @Inject constructor(private val api: TransactionsApi) : TransactionsRepository {

    override suspend fun getUserTransactions(): Result<List<Transaction>> = try {
        val res = api.getUserTransactions()
        if (res.isSuccessful) {
            Result.success(res.body()?.content?.map { it.toDomain() } ?: emptyList())
        } else {
            Result.failure(CardException.NetworkError)
        }
    } catch (e: Exception) {
        Result.failure(CardException.NetworkError)
    }

    private fun TransactionViewDto.toDomain(): Transaction {
        val rawAmount = amount.toDoubleOrNull() ?: 0.0
        val type = when {
            rawAmount < 0 -> TransactionType.OUTCOME
            rawAmount > 0 -> TransactionType.INCOME
            else -> TransactionType.OUTCOME
        }
        return Transaction(
            id = transactionId,
            amount = rawAmount.absoluteValue,
            date = transactionDate,
            description = description,
            type = type
        )
    }
    override suspend fun makeTransaction(input: TransactionInput): Result<Unit> = try {
        val request = MakeTransactionRequest(
            cardId = input.fromCardId,
            cardToTransact = input.toCardNumber,
            amount = input.amount,
            description = input.description
        )
        val response = api.makeTransaction(request)
        if (response.isSuccessful) Result.success(Unit)
        else Result.failure(CardException.NetworkError)
    } catch (e: Exception) {
        Result.failure(CardException.NetworkError)
    }
    override suspend fun getCardTransactions(cardId: Long): Result<List<Transaction>> = try {
        val response = api.getCardTransactions(cardId)
        if (response.isSuccessful) {
            val txList = response.body()?.content?.mapNotNull { dto -> dto.toDomain() } ?: emptyList()
            Result.success(txList)
        } else {
            Result.failure(CardException.NetworkError)
        }
    } catch (e: Exception) {
        Result.failure(CardException.NetworkError)
    }
}