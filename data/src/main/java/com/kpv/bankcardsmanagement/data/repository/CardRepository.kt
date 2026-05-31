package com.kpv.bankcardsmanagement.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.kpv.bankcardsmanagement.data.api.CardsApi
import com.kpv.bankcardsmanagement.data.dto.CardConditionDto
import com.kpv.bankcardsmanagement.data.dto.CardEnteredRequest
import com.kpv.bankcardsmanagement.data.dto.CardToShowDto
import com.kpv.bankcardsmanagement.data.dto.DailySpendingDto
import com.kpv.bankcardsmanagement.data.dto.TransactionViewDto
import com.kpv.bankcardsmanagement.domain.core.exceptions.CardException
import com.kpv.bankcardsmanagement.domain.core.model.Card
import com.kpv.bankcardsmanagement.domain.core.model.CardConditionInfo
import com.kpv.bankcardsmanagement.domain.core.model.CardInput
import com.kpv.bankcardsmanagement.domain.core.model.CardStatus
import com.kpv.bankcardsmanagement.domain.core.model.DailySpending
import com.kpv.bankcardsmanagement.domain.core.model.Transaction
import com.kpv.bankcardsmanagement.domain.core.model.TransactionType
import com.kpv.bankcardsmanagement.domain.repository.CardsRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlin.collections.map
import kotlin.math.absoluteValue

class CardsRepositoryImpl @Inject constructor(private val api: CardsApi) : CardsRepository {

    override suspend fun getUserCards(): Result<List<Card>> = try {
        val res = api.getAllCards()

        android.util.Log.d("CardsRepo", "Response code: ${res.code()}, body: ${res.body()?.content?.size} cards")

        if (res.isSuccessful) {
            val cards = res.body()?.content?.map { dto ->
                android.util.Log.d("CardsRepo", "Mapping card: ${dto.cardMask}, balance type: ${dto.balance::class.simpleName}")
                dto.toDomain()
            } ?: emptyList()

            android.util.Log.d("CardsRepo", "Mapped ${cards.size} cards successfully")
            Result.success(cards)
        } else {
            android.util.Log.e("CardsRepo", "HTTP error: ${res.code()}, message: ${res.message()}")
            Result.failure(CardException.NetworkError)
        }

    } catch (e: kotlinx.serialization.SerializationException) {
        android.util.Log.e("CardsRepo", "Serialization error: ${e.message}", e)
        Result.failure(CardException.NetworkError)

    } catch (e: Exception) {
        android.util.Log.e("CardsRepo", "Unexpected error", e)
        Result.failure(CardException.NetworkError)
    }

    override suspend fun getCardTransactions(cardId: Long): Result<List<Transaction>> = try {
        val res = api.getTransactions(cardId)
        Result.success(res.body()?.content?.map { it.toDomain() } ?: emptyList())
    } catch (e: Exception) { Result.failure(CardException.NetworkError) }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeeklySpending(cardId: Long): Result<List<DailySpending>> = try {
        val now = LocalDate.now()
        val weekAgo = now.minusDays(6)
        val res = api.getSpending(cardId, weekAgo.toString(), now.toString())
        Result.success(res.body()?.days?.map { it.toDomain() } ?: emptyList())
    } catch (e: Exception) { Result.failure(CardException.NetworkError) }

    override suspend fun blockCard(cardId: Long): Result<Unit> = try {
        if (api.blockCard(cardId).isSuccessful) Result.success(Unit)
        else Result.failure(CardException.NetworkError)
    } catch (e: Exception) { Result.failure(CardException.NetworkError) }

    override suspend fun addCard(input: CardInput): Result<Unit> = try {
        val req = CardEnteredRequest(input.cardNumber.replace(" ", ""), input.expireDate)
        if (api.addCard(req).isSuccessful) Result.success(Unit)
        else Result.failure(CardException.NetworkError)
    } catch (e: Exception) { Result.failure(CardException.NetworkError) }

    private fun CardToShowDto.toDomain(): Card = Card(
        cardId = cardId,
        cardMask = cardMask,
        expireDate = expireDate,
        balance = balance,
        condition = cardCondition?.toConditionDomain() ?: CardConditionInfo(CardStatus.ACTIVE, null, null)
    )

    private fun CardConditionDto.toConditionDomain(): CardConditionInfo = CardConditionInfo(
        status = runCatching { CardStatus.valueOf(conditionName ?: "ACTIVE") }.getOrDefault(CardStatus.PENDING),
        comment = comment,
        dateOfCondition = dateOfCondition
    )

    private fun DailySpendingDto.toDomain(): DailySpending = DailySpending(
        date = date,
        amount = amount.toDoubleOrNull()?.absoluteValue ?: 0.0
    )
    private fun TransactionViewDto.toDomain(): Transaction {
        val rawAmount = amount.toDoubleOrNull() ?: 0.0

        val transactionType = when {
            rawAmount <= 0 -> TransactionType.OUTCOME
            rawAmount > 0 -> TransactionType.INCOME
            else -> {TransactionType.OUTCOME}
        }

        return Transaction(
            id = transactionId,
            amount = rawAmount.absoluteValue,
            date = transactionDate,
            description = description,
            type = transactionType
        )
    }
}