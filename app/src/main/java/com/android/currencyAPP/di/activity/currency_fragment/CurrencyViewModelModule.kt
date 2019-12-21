package com.android.currencyAPP.di.activity.currency_fragment

import androidx.lifecycle.ViewModel
import com.android.currencyAPP.di.activity.ViewModelKey
import com.android.currencyAPP.ui.fragments.currency_converter_fragment.CurrencyFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CurrencyViewModelModule {
    @Binds
    @CurrencyFragmentScope
    @IntoMap
    @ViewModelKey(CurrencyFragmentViewModel::class)
    abstract fun currencyViewModelBind(viewModel: CurrencyFragmentViewModel): ViewModel
}