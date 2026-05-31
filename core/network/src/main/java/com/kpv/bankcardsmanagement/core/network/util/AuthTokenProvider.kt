package com.kpv.bankcardsmanagement.core.network.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlin.getValue
import androidx.core.content.edit
import com.kpv.bankcardsmanagement.core.network.dto.Jwts
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenProvider @Inject constructor(
    private val application: Application
) {
    private val prefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            application,
            "auth_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    companion object {
        private const val KEY_JWT = "jwt_token"
        private const val KEY_REFRESH = "refresh_token"
    }

    fun saveTokens(jwt: String?, refreshToken: String?) {
        prefs.edit().apply {
            putString(KEY_JWT, jwt)
            putString(KEY_REFRESH, refreshToken)
            apply()
        }
    }

    fun getJwtToken(): String? = prefs.getString(KEY_JWT, null)
    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH, null)
    fun getTokens(): Jwts = Jwts(getJwtToken(), getRefreshToken())

    fun clearAuthData() {
        prefs.edit { clear() }
    }

    fun isAuthorized(): Boolean = getJwtToken() != null
}