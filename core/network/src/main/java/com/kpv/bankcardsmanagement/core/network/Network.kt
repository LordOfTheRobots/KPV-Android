package com.kpv.bankcardsmanagement.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kpv.bankcardsmanagement.core.network.qualifiers.MainRetrofit
import kotlinx.serialization.json.Json
import com.kpv.bankcardsmanagement.core.network.util.AuthInterceptor
import com.kpv.bankcardsmanagement.core.network.util.AuthTokenProvider
import com.kpv.bankcardsmanagement.core.network.util.CookieJarImpl
import dagger.Module
import dagger.Provides
import okhttp3.CookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object NetworkModule {

    private const val JWT_PREFIX = "Bearer "

    @Provides
    @Singleton
    fun provideCookieJar(): CookieJar {
        return CookieJarImpl()
    }

    @Provides
    @Singleton
    @MainRetrofit
    fun provideOkHttp(
        cookieJar: CookieJar,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenProvider: AuthTokenProvider): AuthInterceptor {
        return AuthInterceptor(tokenProvider)
    }
    @MainRetrofit
    @Provides
    @Singleton
    fun provideRetrofit(@MainRetrofit okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            prettyPrint = false
        }

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}

