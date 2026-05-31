package com.kpv.bankcardsmanagement.core.maps

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kpv.bankcardsmanagement.core.maps.api.YandexGeocodeApi
import com.kpv.bankcardsmanagement.core.maps.interceptor.YandexApiKeyInterceptor
import com.kpv.bankcardsmanagement.core.maps.qualifiers.MapsRetrofit
import kotlinx.serialization.json.Json
import dagger.Module
import dagger.Provides
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object Maps {
    @Provides
    @Singleton
    fun provideYandexGeocodeApi(@MapsRetrofit retrofit: Retrofit): YandexGeocodeApi{
        return retrofit.create(YandexGeocodeApi::class.java)
    }
    @Provides
    @Singleton
    @MapsRetrofit
    fun provideOkHttp(
        apiInterceptor: YandexApiKeyInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiInterceptor)
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
    fun provideApiKeyInterceptor(): YandexApiKeyInterceptor {
        return YandexApiKeyInterceptor(BuildConfig.YANDEX_API_KEY)
    }
    @MapsRetrofit
    @Provides
    @Singleton
    fun provideRetrofit(@MapsRetrofit okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.YANDEX_API_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}