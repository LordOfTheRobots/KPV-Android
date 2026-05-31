package com.kpv.bankcardsmanagement.core.maps.interceptor

import com.kpv.bankcardsmanagement.core.maps.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class YandexApiKeyInterceptor @Inject constructor(
    private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newUrl = original.url.newBuilder()
            .addQueryParameter("apikey", apiKey)
            .build()
        val newRequest = original.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}