package com.kpv.bankcardsmanagement.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kpv.bankcardsmanagement.core.network.BuildConfig
import com.kpv.bankcardsmanagement.core.network.qualifiers.MainRetrofit
import com.kpv.bankcardsmanagement.core.network.util.AuthInterceptor
import com.kpv.bankcardsmanagement.core.network.util.AuthTokenProvider
import com.kpv.bankcardsmanagement.data.api.AuthApi
import com.kpv.bankcardsmanagement.data.api.CardsApi
import com.kpv.bankcardsmanagement.data.api.TransactionsApi
import com.kpv.bankcardsmanagement.data.api.UserApi
import com.kpv.bankcardsmanagement.data.repository.AuthRepositoryImpl
import com.kpv.bankcardsmanagement.data.repository.CardsRepositoryImpl
import com.kpv.bankcardsmanagement.data.repository.MapsRepositoryImpl
import com.kpv.bankcardsmanagement.data.repository.TransactionsRepositoryImpl
import com.kpv.bankcardsmanagement.data.repository.UserRepositoryImpl
import com.kpv.bankcardsmanagement.domain.repository.AuthRepository
import com.kpv.bankcardsmanagement.domain.repository.CardsRepository
import com.kpv.bankcardsmanagement.domain.repository.MapsRepository
import com.kpv.bankcardsmanagement.domain.repository.TransactionsRepository
import com.kpv.bankcardsmanagement.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.CookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindCardsRepository(impl: CardsRepositoryImpl): CardsRepository

    @Binds @Singleton
    abstract fun bindTransactionsRepository(impl: TransactionsRepositoryImpl): TransactionsRepository

    @Binds @Singleton
    abstract fun bindMapsRepository(impl: MapsRepositoryImpl): MapsRepository

    @Binds @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}