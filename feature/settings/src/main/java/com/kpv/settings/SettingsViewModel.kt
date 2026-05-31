package com.kpv.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.core.ui.theme.UiText
import com.kpv.bankcardsmanagement.domain.core.model.UserProfile
import com.kpv.bankcardsmanagement.domain.usecases.auth.LogoutUseCase
import com.kpv.bankcardsmanagement.domain.usecases.user.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false,
    val isLoggingOut: Boolean = false,
    val logoutSuccess: Boolean = false,
    val error: UiText? = null,
    val showLogoutDialog: Boolean = false
)

class SettingsViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init { loadProfile() }

    fun loadProfile() {
        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isLoading = true, error = null) }
            getUserProfileUseCase()
                .onSuccess { profile ->
                    _state.update { currentState ->
                        currentState.copy(userProfile = profile, isLoading = false)
                    }
                }
                .onFailure { exception ->
                    _state.update { currentState ->
                        currentState.copy(isLoading = false, error = mapError(exception))
                    }
                }
        }
    }

    fun toggleLogoutDialog(show: Boolean) = _state.update { currentState -> currentState.copy(showLogoutDialog = show) }
    fun dismissError() = _state.update { currentState -> currentState.copy(error = null) }
    fun consumeLogoutSuccess() = _state.update { currentState -> currentState.copy(logoutSuccess = false) }

    fun logout() {
        viewModelScope.launch {
            _state.update { currentState -> currentState.copy(isLoggingOut = true, error = null) }

            logoutUseCase()

            _state.update { currentState ->
                currentState.copy(isLoggingOut = false, logoutSuccess = true, showLogoutDialog = false)
            }
        }
    }

    private fun mapError(e: Throwable): UiText = StringsObject.errorLoadingProfile
}