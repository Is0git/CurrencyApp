package com.android.currencyAPP.di

import android.app.Application
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object WorkManagerModule {
    @Singleton
    @Provides
    @JvmStatic
    fun getWorkManager(application: Application) = WorkManager.getInstance(application)
}