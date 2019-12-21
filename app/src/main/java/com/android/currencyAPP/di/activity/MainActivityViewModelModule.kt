package com.android.currencyAPP.di.activity

import androidx.lifecycle.ViewModel
import com.android.currencyAPP.ui.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    @MainActivityScope
    abstract fun bindActivityViewModel(viewModel: MainActivityViewModel): ViewModel
}