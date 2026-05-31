package com.kpv.bankcardsmanagement.auth.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.kpv.bankcardsmanagement.auth.model.AuthCredentials
import com.kpv.bankcardsmanagement.domain.core.exceptions.AuthException
import com.kpv.bankcardsmanagement.domain.usecases.auth.LoginUseCase
import com.kpv.bankcardsmanagement.domain.usecases.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Exception
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    val registerUseCase: RegisterUseCase,
    val loginUseCase: LoginUseCase
): ViewModel() {

    suspend fun login(credentials: AuthCredentials): AuthResult {
        return try {

            val result = loginUseCase(credentials.email, credentials.password)
            result.getOrThrow()

            AuthResult.Success

        } catch (e: AuthException) {
            AuthResult.Error(e)
        } catch (e: Exception) {
            AuthResult.Error(AuthException.NetworkError)
        }
    }

    suspend fun register(credentials: AuthCredentials): AuthResult {
        return try {

            val result = registerUseCase(credentials.email, credentials.password)
            result.getOrThrow()

            AuthResult.Success

        } catch (e: AuthException) {
            AuthResult.Error(e)
        } catch (e: Exception) {
            AuthResult.Error(AuthException.NetworkError)
        }
    }

}



@Stable
sealed interface AuthResult {
    data object Success : AuthResult
    data class Error(val error: AuthException) : AuthResult
}