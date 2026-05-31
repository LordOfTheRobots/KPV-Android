package com.kpv.bankcardsmanagement.core.network.util

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieJarImpl : CookieJar {

    private val cache = mutableMapOf<String, List<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.find { it.name == "refreshToken" }?.let {
            cache[url.host] = listOf(it)
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cache[url.host] ?: emptyList()
    }

    fun clear() {
        cache.clear()
    }
}