package com.android.currencyAPP.di

import android.app.Application
import com.android.currencyAPP.App
import com.android.currencyAPP.di.activity.WorkerModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Component(modules = [AndroidInjectionModule::class, ActivityBuilder::class, RetrofitModule::class, RoomModule::class, WorkManagerModule::class, WorkerModule::class])
@Singleton
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder
    }
}