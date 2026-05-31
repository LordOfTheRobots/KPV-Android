package com.kpv.bankcardsmanagement.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.kpv.bankcardsmanagement.core.database.yandex.yandex.DatabaseYandexModule
import com.kpv.bankcardsmanagement.core.maps.Maps
import com.kpv.bankcardsmanagement.core.network.NetworkModule
import com.kpv.bankcardsmanagement.data.DataModule
import com.kpv.bankcardsmanagement.data.NetworkApiModule
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, NetworkModule::class, Maps::class,
    DatabaseYandexModule::class, ViewModelsModule::class, NetworkApiModule::class,
    ViewModelFactoryModule::class, AppModule::class,
])
interface AppComponent {
    fun activityComponentFactory(): ActivityComponent.Factory
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}