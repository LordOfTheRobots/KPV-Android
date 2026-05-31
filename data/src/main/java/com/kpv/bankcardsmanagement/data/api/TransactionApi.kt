package com.kpv.bankcardsmanagement.data.api

import com.kpv.bankcardsmanagement.core.common.page.PageResponse
import com.kpv.bankcardsmanagement.data.dto.MakeTransactionRequest
import com.kpv.bankcardsmanagement.data.dto.TransactionViewDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionsApi {
    @GET("/api/v1/user/transactions")
    suspend fun getUserTransactions(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 100
    ): Response<PageResponse<TransactionViewDto>>

    @POST("/api/v1/transaction/make-transaction")
    suspend fun makeTransaction(@Body request: MakeTransactionRequest): Response<Unit>
    @GET("/api/v1/cards/{cardId}/transactions")
    suspend fun getCardTransactions(
        @Path("cardId") cardId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 50
    ): Response<PageResponse<TransactionViewDto>>
}