package com.kpv.bankcardsmanagement.feature.cards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.core.ui.theme.UiText
import com.kpv.bankcardsmanagement.domain.core.exceptions.CardException
import com.kpv.bankcardsmanagement.domain.core.model.Card
import com.kpv.bankcardsmanagement.domain.core.model.CardInput
import com.kpv.bankcardsmanagement.domain.core.model.DailySpending
import com.kpv.bankcardsmanagement.domain.core.model.Transaction
import com.kpv.bankcardsmanagement.domain.usecases.cards.*
import com.kpv.bankcardsmanagement.domain.usecases.transactions.GetCardTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

data class CardsUiState(
    val cards: List<Card> = emptyList(),
    val totalBalance: Double = 0.0,
    val selectedCard: Card? = null,
    val transactions: List<Transaction> = emptyList(),
    val weeklySpending: List<DailySpending> = emptyList(),
    val isLoading: Boolean = false,
    val isActionLoading: Boolean = false,
    val error: UiText? = null,
    val showAddDialog: Boolean = false,
    val isAdding: Boolean = false
)

class CardsViewModel @Inject constructor(
    private val addCardUsecase: AddCardUseCase,
    private val getCardsUseCase: GetCardsUseCase,
    private val blockCardUseCase: BlockCardUseCase,
    private val getCardTransactionsUseCase: GetCardTransactionsUseCase,
    private val getSpendingUseCase: GetWeeklySpendingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CardsUiState())
    val state: StateFlow<CardsUiState> = _state.asStateFlow()
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    init { loadCards() }

    fun loadCards() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                var cards: List<Card>? = getCardsUseCase().getOrNull()
                if (cards == null){
                     cards = emptyList()
                }
                _state.update {
                    it.copy(
                        cards = cards,
                        isLoading = false,
                        selectedCard = null,
                        totalBalance = cards.sumOf { it.balance }
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = StringsObject.errorUnknown) }
            }
        }
    }

    fun openCardDetails(cardId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val txResult = getCardTransactionsUseCase(cardId)

                _state.update {
                    it.copy(
                        selectedCard = it.cards.find { c -> c.cardId == cardId },
                        transactions = txResult.getOrNull() ?: emptyList(),
                        isLoading = false,
                        isActionLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun closeCardDetails() {
        _state.update { it.copy(selectedCard = null, transactions = emptyList()) }
    }

    private fun loadCardDetails(cardId: Long) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(isActionLoading = true, error = null)
            }

            val txResult = getCardTransactionsUseCase(cardId)
            val spResult = getSpendingUseCase(cardId)

            txResult.onSuccess { txList ->
                _state.update { currentState ->
                    currentState.copy(transactions = txList)
                }
            }

            spResult.onSuccess { spendingList ->
                _state.update { currentState ->
                    currentState.copy(weeklySpending = spendingList)
                }
            }

            val txError = txResult.exceptionOrNull()
            val spError = spResult.exceptionOrNull()

            if (txError != null || spError != null) {
                val errorToMap = txError ?: spError!!
                _state.update { currentState ->
                    currentState.copy(error = mapError(errorToMap))
                }
            }

            _state.update { currentState ->
                currentState.copy(isActionLoading = false)
            }
        }
    }

    fun blockCard(cardId: Long) {
        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isActionLoading = true, error = null) }

            blockCardUseCase(cardId)
                .onSuccess {
                    _state.update { currentState ->
                        currentState.copy(isActionLoading = false, selectedCard = null)
                    }
                    loadCards()
                }
                .onFailure { exception ->
                    _state.update { currentState ->
                        currentState.copy(isActionLoading = false, error = mapError(exception))
                    }
                }
        }
    }

    fun addCard(input: CardInput) {
        viewModelScope.launch {
            _state.update { it.copy(isAdding = true, error = null) }
            addCardUsecase(input)
            _state.update { it.copy(isAdding = false, showAddDialog = false) }
            loadCards()
        }
    }

    fun toggleAddDialog(show: Boolean) = _state.update { it.copy(showAddDialog = show, error = null) }
    fun dismissError() = _state.update { it.copy(error = null) }

    private fun mapError(e: Throwable): UiText = when (e) {
        is CardException.InvalidNumber -> StringsObject.errorInvalidNumber
        is CardException.InvalidDate -> StringsObject.errorInvalidDate
        is CardException.NetworkError -> StringsObject.errorNetwork
        else -> StringsObject.errorUnknown
    }
}