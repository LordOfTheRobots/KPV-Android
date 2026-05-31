package com.kpv.bankcardsmanagement.domain.usecases.cards

import com.kpv.bankcardsmanagement.domain.repository.CardsRepository
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(private val repo: CardsRepository) {
    suspend operator fun invoke() = repo.getUserCards()
}