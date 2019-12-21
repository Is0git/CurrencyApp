package com.android.currencyAPP.di.activity

import androidx.lifecycle.ViewModelProvider
import com.android.currencyAPP.util.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    @MainActivityScope
    abstract fun viewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}