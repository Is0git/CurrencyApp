package com.android.currencyAPP.ui.fragments.currency_converter_fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.toLiveData
import com.android.currencyAPP.data.database.CurrencyDatabase
import com.android.currencyAPP.data.database.entities.Conversion
import com.android.currencyAPP.data.database.entities.User
import com.android.currencyAPP.di.activity.currency_fragment.CurrencyFragmentScope
import com.android.currencyAPP.util.ConverterAction
import com.android.currencyAPP.util.ConverterState
import com.android.currencyAPP.util.TimeHelper
import kotlinx.coroutines.*
import javax.inject.Inject

const val BASE_CURRENCY = "EUR"
const val BASE_PERCENTAGE = 0.007
const val CONVERSION_LIST_PAGE_SIZE = 10

@CurrencyFragmentScope
class ConverterFragmentRepository @Inject constructor(
    db: CurrencyDatabase,
    val application: Application
) : CurrencyHandle() {

    var dao = db.currencyDao()
    var userHasFreeFees = false
    var userLiveData: LiveData<User> = dao.getUser(1)
    var statesLiveData = MutableLiveData<ConverterState>()
    var conversionResult =
        MutableLiveData<Conversion>(Conversion(0, null, null, null, 0.00, 0.00, 0.00, 0.00, 0))
    var conversionsList = dao.getAllConversions(1).toLiveData(CONVERSION_LIST_PAGE_SIZE)

    fun resetConvertState() {
        resetValues()
        statesLiveData.value = ConverterState.Completed
    }

    fun resetValues() {
        conversionResult.value = Conversion(0, null, null, null, 0.00, 0.00, 0.00, 0.00, 0)
    }

    suspend fun manageAction(      // all action with currencies are going to be here
        fromCurrencyType: String,
        toCurrencyType: String,
        valueFrom: Double,
        action: ConverterAction
    ) = coroutineScope {

        resolveIfFeeIsFree()                     // if it is determined that user has a free fee the value is 0, otherwise fee is going to be base percentage value

        val fromTypeRatio =
            async(Dispatchers.IO) { dao.getCurrency(fromCurrencyType) }                     //get ratios here because all action cases will need these values
        val toTypeRatio =
            async(Dispatchers.IO) { dao.getCurrency(toCurrencyType) }                         // have to do query during every time during convert/receive value check,
        val balance =
            async(Dispatchers.IO) { dao.getBalance(fromCurrencyType) }                            //  because currencies may have been changed


        if (balance.await() == 0.00) {
            statesLiveData.postValue(ConverterState.OnBalanceZero)
            throw CancellationException("no money!")
        }                         //no point to keep computing, we also can change ui values here if needed

        calculate(
            userHasFreeFees,
            valueFrom,
            balance.await(),
            fromCurrencyType,
            toCurrencyType,
            toTypeRatio.await(),
            fromTypeRatio.await()
        ) // calculate all the values(receive, cost etc) with current info


        when (action) {                                                          //performing action with calculated values
            is ConverterAction.Convert -> convert(fromCurrencyType, toCurrencyType)
            is ConverterAction.PeekReceive -> conversionResult.postValue(
                Conversion(
                    0,
                    fromCurrencyType,
                    toCurrencyType,
                    null,
                    sendValue,
                    receiveValue,
                    fee,
                    totalCost,
                    0
                )
            )
        }
    }


    private fun resolveIfFeeIsFree() {
        userHasFreeFees = when {
            if (userLiveData.value?.feesLeft != null) userLiveData.value?.feesLeft!! > 0 else false -> true //Here we write conditions to get 0% fees (can be every 10th<fees are saved in database so it's not hard to implement it here>)
            //dao.checkConvertionsSize(userId) % 10 == 0 -> true.also {isTenth = true}   // or just user can be given a number of free fees)
            else -> false
        }
        feePercentage = if (userHasFreeFees) 0.00 else BASE_PERCENTAGE
    }


    suspend fun convert(
        fromCurrencyType: String,
        toCurrencyType: String
    ) = coroutineScope {
        statesLiveData.postValue(ConverterState.OnConvertStart)

        if (userHasFreeFees) launch(Dispatchers.IO) { dao.decreaseFee(1) }

        //Update everything at the end just in case coroutine is canceled
        //?? maybe should have used service since it's work with money and user might close the app in middle of currency exchange.
        delay(1000)
        updateBalanceInDb(toCurrencyType, receiveValue, fromCurrencyType, totalCost)
        insertConversionToDb(fromCurrencyType, toCurrencyType)
        statesLiveData.postValue(
            ConverterState.OnConvertFinish(
                receiveValue,
                sendValue,
                totalCost,
                fee,
                fromCurrencyType,
                toCurrencyType
            )
        )
        //for animation and prevent spamming
    }


    private suspend fun updateBalanceInDb(
        toType: String,
        sendValue: Double,
        fromType: String,
        totalCost: Double
    ) = coroutineScope {
        launch(Dispatchers.IO) {
            dao.updateValue(
                toType,
                sendValue
            )
        }   //Update user balance in database
        launch(Dispatchers.IO) { dao.decreaseValue(fromType, totalCost) }
    }

    private suspend fun insertConversionToDb(fromCurrencyType: String, toCurrencyType: String) =
        coroutineScope {
            launch(Dispatchers.IO) {
                dao.addConversion(
                    Conversion(
                        0,
                        fromCurrencyType,
                        toCurrencyType,
                        TimeHelper.getTimeInLong(),
                        sendValue,
                        receiveValue,
                        fee,
                        totalCost,
                        1
                    )
                )
            }
        }


}