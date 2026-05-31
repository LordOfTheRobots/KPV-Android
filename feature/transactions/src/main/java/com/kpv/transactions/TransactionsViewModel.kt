package com.kpv.transactions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.core.ui.theme.UiText
import com.kpv.bankcardsmanagement.domain.core.model.Transaction
import com.kpv.bankcardsmanagement.domain.core.model.TransactionType
import com.kpv.bankcardsmanagement.domain.usecases.transactions.GetUserTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.filter

enum class TransactionFilter { ALL, INCOME, EXPENSE }

data class TransactionsUiState(
    val transactions: List<Transaction> = emptyList(),
    val filter: TransactionFilter = TransactionFilter.ALL,
    val monthlyIncome: String = "",
    val monthlyExpenses: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null
)


class TransactionsViewModel @Inject constructor(
    private val getUserTransactionsUseCase: GetUserTransactionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsUiState())
    val state: StateFlow<TransactionsUiState> = _state.asStateFlow()

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    private val currentMonthPrefix = LocalDate.now().toString().substring(0, 7) // "YYYY-MM"

    init { loadTransactions() }

    fun loadTransactions() {
        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isLoading = true, error = null) }

            getUserTransactionsUseCase()
                .onSuccess { txList ->
                    calculateMonthSummary(txList)
                    _state.update { currentState ->
                        currentState.copy(transactions = txList, isLoading = false)
                    }
                }
                .onFailure { exception ->
                    android.util.Log.e("TxError", "=== TRANSACTION LOAD ERROR ===")
                    android.util.Log.e("TxError", "Exception type: ${exception::class.simpleName}")
                    android.util.Log.e("TxError", "Exception message: ${exception.message}")
                    android.util.Log.e("TxError", "Stacktrace:", exception)

                    val uiError = mapError(exception)
                    android.util.Log.e("TxError", "Mapped UI error: ${uiError}")

                    _state.update { currentState ->
                        currentState.copy(isLoading = false, error = uiError)
                    }
                }
        }
    }

    private fun calculateMonthSummary(allTx: List<Transaction>) {
        val currentMonthTx = allTx.filter { tx -> tx.date.startsWith(currentMonthPrefix) }
        val income = currentMonthTx.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val expense = currentMonthTx.filter { it.type == TransactionType.OUTCOME }.sumOf { it.amount }

        _state.update { currentState ->
            currentState.copy(
                monthlyIncome = currencyFormat.format(income),
                monthlyExpenses = currencyFormat.format(expense)
            )
        }
    }

    fun setFilter(filter: TransactionFilter) {
        _state.update { currentState -> currentState.copy(filter = filter) }
    }

    private fun mapError(e: Throwable): UiText = StringsObject.errorLoading
}