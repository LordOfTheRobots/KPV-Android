package com.kpv.bankcardsmanagement.data

import com.kpv.bankcardsmanagement.core.network.qualifiers.MainRetrofit
import com.kpv.bankcardsmanagement.data.api.AuthApi
import com.kpv.bankcardsmanagement.data.api.CardsApi
import com.kpv.bankcardsmanagement.data.api.TransactionsApi
import com.kpv.bankcardsmanagement.data.api.UserApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
object NetworkApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(@MainRetrofit retrofit: Retrofit): AuthApi = retrofit.create()

    @Provides @Singleton
    fun provideCardApi(@MainRetrofit retrofit: Retrofit): CardsApi = retrofit.create()

    @Provides @Singleton
    fun provideTransactionsApi(@MainRetrofit retrofit: Retrofit): TransactionsApi = retrofit.create()

    @Provides @Singleton
    fun provideUserApi(@MainRetrofit retrofit: Retrofit): UserApi = retrofit.create()
}