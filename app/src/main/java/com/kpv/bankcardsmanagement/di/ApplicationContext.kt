package com.kpv.bankcardsmanagement.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationContext

@Module
object AppModule {

    @Provides @Singleton @ApplicationContext
    fun provideApplicationContext(app: Application): Context = app.applicationContext
}