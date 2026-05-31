package com.kpv.bankcardsmanagement

import android.app.Application
import android.os.Looper
import com.kpv.bankcardsmanagement.di.AppComponent
import com.kpv.bankcardsmanagement.di.DaggerAppComponent
import com.yandex.mapkit.MapKitFactory

class AppApplication : Application() {

    lateinit var appComponent: AppComponent
        private set

    private var isMapKitInitialized = false

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)

        android.os.Handler(Looper.getMainLooper()).post {
            initMapKitSafely()
        }
    }

    private fun initMapKitSafely() {
        if (isMapKitInitialized) return
        try {
            val apiKey = BuildConfig.YANDEX_API_KEY
            if (apiKey.isNotBlank()) {
                MapKitFactory.setApiKey(apiKey)
                MapKitFactory.initialize(this)
                isMapKitInitialized = true
                android.util.Log.d("AppApplication", "MapKit инициализирован в фоне")
            }
        } catch (e: Exception) {
            android.util.Log.e("AppApplication", "MapKit init failed", e)
        }
    }
}
