package com.kpv.bankcardsmanagement

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import com.kpv.bankcardsmanagement.auth.model.AuthCredentials
import com.kpv.bankcardsmanagement.auth.viewmodel.AuthResult
import com.kpv.bankcardsmanagement.auth.viewmodel.AuthViewModel
import com.kpv.bankcardsmanagement.domain.core.exceptions.AuthException
import com.kpv.bankcardsmanagement.domain.usecases.auth.LoginUseCase
import com.kpv.bankcardsmanagement.domain.usecases.auth.RegisterUseCase
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.jupiter.api.BeforeEach

class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    private val loginUseCase: LoginUseCase = mockk()
    private val registerUseCase: RegisterUseCase = mockk()

    @BeforeEach
    fun setup() {
        viewModel = AuthViewModel(registerUseCase, loginUseCase)
    }

    @Test
    fun `login - success returns AuthResult Success`() = runTest {

        val credentials = AuthCredentials("test@example.com", "ValidPass123!")
        coEvery { loginUseCase(any(), any()) } returns Result.success(mockk())

        val result = viewModel.login(credentials)

        assertTrue(result is AuthResult.Success)
        coVerify(exactly = 1) { loginUseCase(credentials.email, credentials.password) }
    }

    @Test
    fun `register - network exception returns AuthResult Error with NetworkError`() = runTest {

        val credentials = AuthCredentials("test@example.com", "ValidPass123!")
        coEvery { registerUseCase(any(), any()) } throws java.lang.Exception("Network fail")

        val result = viewModel.register(credentials)

        assertTrue(result is AuthResult.Error)
        assertEquals(AuthException.NetworkError, (result as AuthResult.Error).error)
        coVerify(exactly = 1) { registerUseCase(credentials.email, credentials.password) }
    }
}