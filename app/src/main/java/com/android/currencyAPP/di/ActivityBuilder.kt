package com.android.currencyAPP.di

import com.android.currencyAPP.di.activity.FragmentBuilder
import com.android.currencyAPP.di.activity.MainActivityScope
import com.android.currencyAPP.di.activity.MainActivityViewModelModule
import com.android.currencyAPP.di.activity.ViewModelFactoryModule
import com.android.currencyAPP.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [FragmentBuilder::class, ViewModelFactoryModule::class, MainActivityViewModelModule::class])
    @MainActivityScope
    abstract fun mainActivity(): MainActivity
}