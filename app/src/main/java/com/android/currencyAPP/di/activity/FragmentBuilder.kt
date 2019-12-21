package com.android.currencyAPP.di.activity

import com.android.currencyAPP.di.activity.currency_fragment.CurrencyFragmentScope
import com.android.currencyAPP.di.activity.currency_fragment.CurrencyViewModelModule
import com.android.currencyAPP.ui.fragments.currency_converter_fragment.CurrencyConverterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector(modules = [CurrencyViewModelModule::class])
    @CurrencyFragmentScope
    abstract fun currencyFragment(): CurrencyConverterFragment
}