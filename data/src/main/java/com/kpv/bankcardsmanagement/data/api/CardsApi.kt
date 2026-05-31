package com.kpv.bankcardsmanagement.data.api

import com.kpv.bankcardsmanagement.core.common.page.PageResponse
import com.kpv.bankcardsmanagement.data.dto.CardEnteredRequest
import com.kpv.bankcardsmanagement.data.dto.CardSpendingByDaysDto
import com.kpv.bankcardsmanagement.data.dto.CardToShowDto
import com.kpv.bankcardsmanagement.data.dto.MakeTransactionRequest
import com.kpv.bankcardsmanagement.data.dto.TransactionViewDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CardsApi {
    @GET("/api/v1/user/cards")
    suspend fun getAllCards(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PageResponse<CardToShowDto>>

    @PATCH("/api/v1/user/cards/{cardId}/block")
    suspend fun blockCard(@Path("cardId") cardId: Long): Response<Unit>

    @GET("/api/v1/cards/{cardId}/transactions")
    suspend fun getTransactions(
        @Path("cardId") cardId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponse<TransactionViewDto>>

    @GET("/api/v1/cards/{cardId}/spending")
    suspend fun getSpending(
        @Path("cardId") cardId: Long,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<CardSpendingByDaysDto>

    @POST("/api/v1/transaction/make-transaction")
    suspend fun makeTransaction(@Body request: MakeTransactionRequest): Response<Unit>

    @POST("/api/v1/user/cards")
    suspend fun addCard(@Body request: CardEnteredRequest): Response<Unit>
}