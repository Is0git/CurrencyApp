package com.android.currencyAPP.ui

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.android.currencyAPP.data.database.CurrencyDatabase
import com.android.currencyAPP.data.database.entities.Balance
import com.android.currencyAPP.data.database.entities.Currencies
import com.android.currencyAPP.data.network.CurrencyService
import com.android.currencyAPP.data.network.models.Currency
import com.android.currencyAPP.di.activity.MainActivityScope
import com.android.currencyAPP.ui.fragments.currency_converter_fragment.BASE_CURRENCY
import com.android.currencyAPP.util.CurrenciesSyncWorker
import com.android.currencyAPP.util.LoadingStates
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject


const val CURRENCIES_SYNC_WORK = " currencies_sync_work"
const val INITIAL_BALANCE = 1000.00

@MainActivityScope
class MainActivityRepository @Inject constructor(
    val db: CurrencyDatabase,
    val retrofit: Retrofit,
    val application: Application,
    val workManager: WorkManager
) {
    var service = retrofit.create(CurrencyService::class.java)
    var dao = db.currencyDao()
    var loadingStatesLiveData = MutableLiveData<LoadingStates>()


    init {
        if (workManager.getWorkInfosByTag(CURRENCIES_SYNC_WORK).get().isNotEmpty()) { // check if work still exists, might have been killed after device restart if api level < 20(Alarm manager case)
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            val work = PeriodicWorkRequestBuilder<CurrenciesSyncWorker>(5, TimeUnit.MINUTES).addTag(
                CURRENCIES_SYNC_WORK
            ).setConstraints(constraints).build()


            workManager.enqueue(work)

        }
    }

    fun getBalances(): LiveData<List<Balance>> {
        return dao.getBalances()
    }

    suspend fun addFees(userId: Int, feesNumber: Int) {
        db.currencyDao().addFees(userId, feesNumber)
    }

    suspend fun updateCurrencyDb(initialBalance: Double = INITIAL_BALANCE) = coroutineScope {
        loadingStatesLiveData.postValue(LoadingStates.START)
        var result: Response<Currency>? = null
        try {
            result = service.getCurrencies()
        } catch (e: IOException) {
            loadingStatesLiveData.postValue(LoadingStates.FAILED)
            Toast.makeText(application, "Connect to internet", Toast.LENGTH_LONG).show()
        }

        if (result == null) {
            loadingStatesLiveData.postValue(LoadingStates.FAILED)
            throw CancellationException("currency result null")
        }
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
                    db.currencyDao().insertBalanceCurrencies(Balance(BASE_CURRENCY, initialBalance))
                    for ((key, _) in map) db.currencyDao().insertBalanceCurrencies(
                        Balance(
                            type = key,
                            balanceValue = 0.00
                        )
                    )
                }

            }
        }
        loadingStatesLiveData.postValue(LoadingStates.FINISH)
    }
}