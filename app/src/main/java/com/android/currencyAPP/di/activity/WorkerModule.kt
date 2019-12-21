package com.android.currencyAPP.di.activity

import com.android.currencyAPP.di.WorkerKey
import com.android.currencyAPP.util.CurrenciesSyncWorker
import com.android.currencyAPP.util.WorkerCreator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class WorkerModule {
    @Binds
    @Singleton
    @IntoMap
    @WorkerKey(CurrenciesSyncWorker::class)
    abstract fun currenciesWorker(worker: CurrenciesSyncWorker.Creator): WorkerCreator
}