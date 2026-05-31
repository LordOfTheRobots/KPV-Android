package com.kpv.bankcardsmanagement.domain.usecases.cards

import com.kpv.bankcardsmanagement.domain.repository.CardsRepository
import javax.inject.Inject

class BlockCardUseCase @Inject constructor(private val repo: CardsRepository) {
    suspend operator fun invoke(cardId: Long) = repo.blockCard(cardId)
}