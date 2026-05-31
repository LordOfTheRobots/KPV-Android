package com.kpv.usecases

import com.kpv.bankcardsmanagement.domain.core.exceptions.AuthException
import com.kpv.bankcardsmanagement.domain.repository.AuthRepository
import com.kpv.bankcardsmanagement.domain.usecases.auth.LoginUseCase
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.BeforeEach


class LoginUseCaseTest {

    private lateinit var useCase: LoginUseCase
    private val repository: AuthRepository = mockk()

    @BeforeEach
    fun setup() {
        useCase = LoginUseCase(repository)
    }

    @Test
    fun `invoke - invalid email returns failure with InvalidCredentials`() = runTest {
        val invalidEmail = "not-an-email"
        val password = "password123"

        val result = useCase(invalidEmail, password)

        assertTrue(result.isFailure)
        assertEquals(
            AuthException.InvalidCredentials,
            (result.exceptionOrNull() as? IllegalArgumentException)?.message
        )
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }
}