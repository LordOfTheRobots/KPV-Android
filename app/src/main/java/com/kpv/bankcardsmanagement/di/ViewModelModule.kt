package com.kpv.bankcardsmanagement.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kpv.bankcardsmanagement.auth.viewmodel.AuthViewModel
import com.kpv.bankcardsmanagement.feature.cards.viewmodel.CardsViewModel
import com.kpv.bankcardsmanagement.maps.MapViewModel
import com.kpv.settings.SettingsViewModel
import com.kpv.transactions.TransactionsViewModel
import com.kpv.transfer.TransfersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds @IntoMap @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuth(vm: AuthViewModel): ViewModel

    @Binds @IntoMap @ViewModelKey(CardsViewModel::class)
    abstract fun bindCards(vm: CardsViewModel): ViewModel

    @Binds @IntoMap @ViewModelKey(MapViewModel::class)
    abstract fun bindMap(vm: MapViewModel): ViewModel

    @Binds @IntoMap @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettings(vm: SettingsViewModel): ViewModel

    @Binds @IntoMap @ViewModelKey(TransactionsViewModel::class)
    abstract fun bindTransactions(vm: TransactionsViewModel): ViewModel

    @Binds @IntoMap @ViewModelKey(TransfersViewModel::class)
    abstract fun bindTransfers(vm: TransfersViewModel): ViewModel
}

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}