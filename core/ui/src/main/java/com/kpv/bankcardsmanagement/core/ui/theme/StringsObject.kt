package com.kpv.bankcardsmanagement.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kpv.bankcardsmanagement.R

data object StringsObject {
    val appName: UiText = UiText.StringResource(R.string.app_name)
    val loginTitle: UiText = UiText.StringResource(R.string.login_title)
    val registerTitle: UiText = UiText.StringResource(R.string.register_title)

    val buttonLogin: UiText = UiText.StringResource(R.string.button_login)
    val buttonRegister: UiText = UiText.StringResource(R.string.button_register)

    val toggleToRegister: UiText = UiText.StringResource(R.string.toggle_to_register)
    val toggleToLogin: UiText = UiText.StringResource(R.string.toggle_to_login)

    val errorPasswordShort: UiText = UiText.StringResource(R.string.error_password_short)
    val errorInvalidCredentials: UiText = UiText.StringResource(R.string.error_invalid_credentials)
    val errorUserExists: UiText = UiText.StringResource(R.string.error_user_exists)
    val errorServer: UiText = UiText.StringResource(R.string.error_server)
    val errorNetwork: UiText = UiText.StringResource(R.string.error_network)
    val errorUnknown: UiText = UiText.StringResource(R.string.error_unknown)

    val loading: UiText = UiText.StringResource(R.string.loading)
    val errorPasswordNoDigits: UiText = UiText.StringResource(R.string.error_password_no_digits)
    val errorPasswordNoSpecialChars: UiText = UiText.StringResource(R.string.error_password_no_special_chars)
    val errorPasswordNoLowercase: UiText = UiText.StringResource(R.string.error_password_no_lowercase)
    val errorPasswordNoUppercase: UiText = UiText.StringResource(R.string.error_password_no_uppercase)
    val email: UiText = UiText.StringResource(R.string.email)
    val password: UiText = UiText.StringResource(R.string.password)
    val navCards: UiText = UiText.StringResource(R.string.nav_cards)
    val navTransactions: UiText = UiText.StringResource(R.string.nav_transactions)
    val navMap: UiText = UiText.StringResource(R.string.nav_map)
    val navSettings: UiText = UiText.StringResource(R.string.nav_settings)

    val totalBalance = UiText.StringResource(R.string.total_balance)
    val myCards = UiText.StringResource(R.string.my_cards)
    val addCard = UiText.StringResource(R.string.add_card)
    val cardNumberHint = UiText.StringResource(R.string.card_number_hint)
    val expireHint = UiText.StringResource(R.string.expire_hint)
    val cancel = UiText.StringResource(R.string.cancel)
    val add = UiText.StringResource(R.string.add)
    val errorInvalidNumber = UiText.StringResource(R.string.error_invalid_number)
    val errorInvalidDate = UiText.StringResource(R.string.error_invalid_date)
    val errorLoading = UiText.StringResource(R.string.error_loading)
    val noCards = UiText.StringResource(R.string.no_cards)

    val setAsPrimary = UiText.StringResource(R.string.set_as_primary)
    val blockCard = UiText.StringResource(R.string.block_card)
    val deleteCard = UiText.StringResource(R.string.delete_card)
    val transactionsLabel = UiText.StringResource(R.string.transactions_label)
    val weeklySpending = UiText.StringResource(R.string.weekly_spending)
    val transactionsTitle = UiText.StringResource(R.string.transactions_title)
    val monthlyIncome = UiText.StringResource(R.string.monthly_income)
    val monthlyExpense = UiText.StringResource(R.string.monthly_expense)
    val filterAll = UiText.StringResource(R.string.filter_all)
    val filterIncome = UiText.StringResource(R.string.filter_income)
    val filterExpense = UiText.StringResource(R.string.filter_expense)
    val noTransactions = UiText.StringResource(R.string.no_transactions)
    val errorLoadingTransactions = UiText.StringResource(R.string.error_loading_transactions)

    val close = UiText.StringResource(R.string.close)
    val details = UiText.StringResource(R.string.details)
    val transferTitle = UiText.StringResource(R.string.transfer_title)
    val fromCardLabel = UiText.StringResource(R.string.from_card_label)
    val toCardLabel = UiText.StringResource(R.string.to_card_label)
    val amountLabel = UiText.StringResource(R.string.amount_label)
    val descriptionLabel = UiText.StringResource(R.string.description_label)
    val sendButton = UiText.StringResource(R.string.send_button)
    val transferSuccess = UiText.StringResource(R.string.transfer_success)
    val transferError = UiText.StringResource(R.string.transfer_error)
    val insufficientFunds = UiText.StringResource(R.string.insufficient_funds)
    val invalidAmount = UiText.StringResource(R.string.invalid_amount)
    val noActiveCards = UiText.StringResource(R.string.no_active_cards)
    val selectSourceCard = UiText.StringResource(R.string.select_source_card)

    val settingsTitle = UiText.StringResource(R.string.settings_title)
    val profileSection = UiText.StringResource(R.string.profile_section)
    val emailLabel = UiText.StringResource(R.string.email_label)
    val phoneLabel = UiText.StringResource(R.string.phone_label)
    val telegramLabel = UiText.StringResource(R.string.telegram_label)
    val roleLabel = UiText.StringResource(R.string.role_label)
    val logoutButton = UiText.StringResource(R.string.logout_button)
    val logoutConfirm = UiText.StringResource(R.string.logout_confirm)
    val errorLoadingProfile = UiText.StringResource(R.string.error_loading_profile)
    val cardBalanceFmt = UiText.StringResource(R.string.card_balance_fmt)

    val searchHint = UiText.StringResource(R.string.search_hint)
    val searchButton = UiText.StringResource(R.string.search_button)
    val searchResults = UiText.StringResource(R.string.search_results)
    val nearbyBanks = UiText.StringResource(R.string.nearby_banks)
    val navigateTo = UiText.StringResource(R.string.navigate_to)
    val errorLoadingMap = UiText.StringResource(R.string.error_loading_map)

    val cardPremium = UiText.StringResource(R.string.card_premium)
    val cardValidThru = UiText.StringResource(R.string.card_valid_thru)
    val cardBalance = UiText.StringResource(R.string.card_balance)
    val cardBankName = UiText.StringResource(R.string.card_bank_name)
    val addCardHint = UiText.StringResource(R.string.add_card_hint)
    val errorInvalidCardNumber = UiText.StringResource(R.string.error_invalid_card_number)
    val retry = UiText.StringResource(R.string.retry)
    val noTransactionsHint = UiText.StringResource(R.string.no_transactions_hint)

}

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(
        val resId: Int,
        val args: Array<Any> = arrayOf()
    ) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }
}