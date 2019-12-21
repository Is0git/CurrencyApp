package com.android.currencyAPP.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.android.currencyAPP.data.database.CurrencyDatabase
import com.android.currencyAPP.data.database.entities.Balance
import com.android.currencyAPP.data.database.entities.Currencies
import com.android.currencyAPP.data.network.CurrencyService
import com.android.currencyAPP.ui.INITIAL_BALANCE
import com.android.currencyAPP.ui.fragments.currency_converter_fragment.BASE_CURRENCY
import kotlinx.coroutines.*
import retrofit2.Retrofit
import javax.inject.Inject


class CurrenciesSyncWorker constructor(
    var context: Context,
    private val params: WorkerParameters,
    private val retrofit: Retrofit,
    val db: CurrencyDatabase
) : CoroutineWorker(context, params) {

    val dao = db.currencyDao()
    override suspend fun doWork(): Result = coroutineScope {
        val job = launch {
            val result = retrofit.create(CurrencyService::class.java).getCurrencies()

            val map = when {
                result.isSuccessful -> result.body()?.rates
                !result.isSuccessful -> throw CancellationException(result.message())
                else -> throw CancellationException("something went wrong")
            }

            val balancesSize = async(Dispatchers.IO) { dao.getBalancesCount() }
            if (!map.isNullOrEmpty()) {
                launch(Dispatchers.IO) {
                    for ((key, value) in map) {
                        db.currencyDao().insertCurrency(Currencies(key, value))

                    }

                    if (balancesSize.await() == 0) {
                        db.currencyDao()
                            .insertBalanceCurrencies(Balance(BASE_CURRENCY, INITIAL_BALANCE))
                        for ((key, _) in map) db.currencyDao().insertBalanceCurrencies(
                            Balance(
                                type = key,
                                balanceValue = 0.00
                            )
                        )
                    }
                }
            }

        }


        job.join()
        Result.success()
    }

    class Creator @Inject constructor(val retrofit: Retrofit, val db: CurrencyDatabase) :
        WorkerCreator {

        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return CurrenciesSyncWorker(appContext, params, retrofit, db)
        }
    }
}