package com.kpv.bankcardsmanagement.domain.usecases.cards

import com.kpv.bankcardsmanagement.domain.core.exceptions.CardException
import com.kpv.bankcardsmanagement.domain.core.model.CardInput
import com.kpv.bankcardsmanagement.domain.repository.CardsRepository
import javax.inject.Inject

class AddCardUseCase @Inject constructor(private val repo: CardsRepository) {
    suspend operator fun invoke(input: CardInput): Result<Unit> {
        val clean = input.cardNumber.replace(" ", "")
        if (clean.length !in 16..19) return Result.failure(CardException.InvalidNumber)
        if (!input.expireDate.matches(Regex("\\d{2}/\\d{2}"))) return Result.failure(CardException.InvalidDate)
        return repo.addCard(input)
    }
}

