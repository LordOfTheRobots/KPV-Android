package com.kpv.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.core.ui.theme.UiText
import com.kpv.bankcardsmanagement.domain.core.model.Card
import com.kpv.bankcardsmanagement.domain.core.model.CardStatus
import com.kpv.bankcardsmanagement.domain.core.model.TransactionInput
import com.kpv.bankcardsmanagement.domain.usecases.cards.GetCardsUseCase
import com.kpv.bankcardsmanagement.domain.usecases.transactions.MakeTransactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

data class TransfersUiState(
    val availableCards: List<Card> = emptyList(),
    val selectedCard: Card? = null,
    val toCardNumber: String = "",
    val amount: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val isCardsLoading: Boolean = false,
    val error: UiText? = null,
    val success: Boolean = false,
    val dropdownExpanded: Boolean = false
)

class TransfersViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val makeTransactionUseCase: MakeTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransfersUiState())
    val state: StateFlow<TransfersUiState> = _state.asStateFlow()

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    init { loadActiveCards() }

    private fun loadActiveCards() {
        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isCardsLoading = true, error = null) }
            getCardsUseCase()
                .onSuccess { cardsList ->
                    val active = cardsList.filter { it.condition.status == CardStatus.ACTIVE }
                    _state.update { currentState ->
                        currentState.copy(availableCards = active, isCardsLoading = false)
                    }
                }
                .onFailure { exception ->
                    _state.update { currentState ->
                        currentState.copy(isCardsLoading = false, error = mapError(exception))
                    }
                }
        }
    }

    fun selectCard(card: Card) {
        _state.update { currentState ->
            currentState.copy(selectedCard = card, dropdownExpanded = false, error = null)
        }
    }

    fun toggleDropdown(expanded: Boolean) = _state.update { currentState -> currentState.copy(dropdownExpanded = expanded) }
    fun updateToCard(value: String) = _state.update { currentState -> currentState.copy(toCardNumber = value, error = null) }
    fun updateAmount(value: String) = _state.update { currentState -> currentState.copy(amount = value, error = null) }
    fun updateDescription(value: String) = _state.update { currentState -> currentState.copy(description = value, error = null) }

    fun sendTransfer() {
        viewModelScope.launch {
            val currentState = _state.value
            val amountVal = currentState.amount.replace(",", ".").toDoubleOrNull()

            when {
                currentState.selectedCard == null -> {
                    _state.update { currentState.copy(error = StringsObject.selectSourceCard) }
                    return@launch
                }
                amountVal == null || amountVal <= 0 -> {
                    _state.update { currentState.copy(error = StringsObject.invalidAmount) }
                    return@launch
                }
            }

            _state.update { currentState -> currentState.copy(isLoading = true, error = null) }

            val input = TransactionInput(
                fromCardId = currentState.selectedCard!!.cardId,
                toCardNumber = currentState.toCardNumber,
                amount = currentState.amount.replace(",", "."),
                description = currentState.description
            )

            makeTransactionUseCase(input)
                .onSuccess {
                    _state.update { currentState -> currentState.copy(isLoading = false, success = true) }
                }
                .onFailure { exception ->
                    val errorMsg = when {
                        exception.message?.contains("balance", ignoreCase = true) == true -> StringsObject.insufficientFunds
                        else -> StringsObject.transferError
                    }
                    _state.update { currentState -> currentState.copy(isLoading = false, error = errorMsg) }
                }
        }
    }

    fun consumeResult() = _state.update { currentState -> currentState.copy(success = false, error = null) }

    private fun mapError(e: Throwable): UiText = when {
        e.message?.contains("404") == true -> StringsObject.noActiveCards
        else -> StringsObject.transferError
    }
}