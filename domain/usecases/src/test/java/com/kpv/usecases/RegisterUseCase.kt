package com.kpv.usecases

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.kpv.bankcardsmanagement.domain.core.exceptions.AuthException
import com.kpv.bankcardsmanagement.domain.core.model.AuthTokens
import com.kpv.bankcardsmanagement.domain.repository.AuthRepository
import com.kpv.bankcardsmanagement.domain.usecases.auth.RegisterUseCase
import io.mockk.coVerify

class RegisterUseCaseTest {

    private lateinit var useCase: RegisterUseCase
    private val repository: AuthRepository = mockk()

    @BeforeEach
    fun setup() {
        useCase = RegisterUseCase(repository)
    }

    @Test
    fun `invoke - valid credentials calls repository with trimmed email`() = runTest {
        val email = "  user@example.com  "
        val password = "ValidPass123!"
        val expectedTokens = mockk<AuthTokens>()
        coEvery { repository.register(email.trim(), password) } returns Result.success(expectedTokens)

        val result = useCase(email, password)

        assertTrue(result.isSuccess)
        assertEquals(expectedTokens, result.getOrNull())
        coVerify(exactly = 1) { repository.register(email.trim(), password) }
    }
}