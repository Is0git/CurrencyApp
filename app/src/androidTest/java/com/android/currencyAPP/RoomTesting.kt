package com.android.currencyAPP

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.android.currencyAPP.data.database.CurrencyDatabase
import com.android.currencyAPP.data.database.dao.CurrencyDao
import com.android.currencyAPP.data.database.entities.Balance
import com.android.currencyAPP.data.database.entities.Currencies
import com.android.currencyAPP.data.database.entities.User
import com.android.currencyAPP.ui.fragments.currency_converter_fragment.ConverterFragmentRepository
import com.android.currencyAPP.ui.fragments.currency_converter_fragment.CurrencyFragmentViewModel
import com.android.currencyAPP.util.ConverterAction
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class RoomTesting {

    lateinit var database: CurrencyDatabase
    lateinit var dao: CurrencyDao
    lateinit var currencyViewModel: CurrencyFragmentViewModel

    @Before
    fun setup() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder(context, CurrencyDatabase::class.java)
            .allowMainThreadQueries().build().also { dao = it.currencyDao() }
        currencyViewModel =
            CurrencyFragmentViewModel(ConverterFragmentRepository(database, context))
        insertPreTest()
    }


    suspend fun insertPreTest() {
        dao.insertUser(User(2))
        dao.insertCurrency(Currencies("USD", 1.13))
        dao.insertBalanceCurrencies(Balance("USD", 1000.00))
        dao.insertCurrency(Currencies("GBP", 0.85))
        dao.insertBalanceCurrencies(Balance("GBP", 1000.00))
        dao.insertBalanceCurrencies(Balance("EUR", 1000.00))
    }


    @Test
    fun regularConvertFromBase() = runBlocking {
        dao.updateUser(User(0, 1))
        currencyViewModel.converterFragmentRepository.manageAction(
            "EUR",
            "USD",
            500.00,
            ConverterAction.Convert
        )
        val result = dao.getAllConversionsForTesting(1).first()
        val cost = result.cost
        val receiveValue = result.received
        val sendValue = result.send
        val fee = result.fee


        Assert.assertEquals(503.50, cost, 0.00)
        Assert.assertEquals(565.00, receiveValue!!, 0.00)
        Assert.assertEquals(500.00, sendValue!!, 0.00)
        Assert.assertEquals(3.5, fee, 0.00)

    }

    @Test
    fun regularConvertFromNotBase() = runBlocking {
        dao.updateUser(User(0, 1))
        currencyViewModel.converterFragmentRepository.manageAction(
            "GBP",
            "USD",
            500.00,
            ConverterAction.Convert
        )

        val result = dao.getAllConversionsForTesting(1).first()
        val cost = result.cost
        val receiveValue = result.received
        val sendValue = result.send
        val fee = result.fee


        Assert.assertEquals(503.50, cost, 0.00)
        Assert.assertEquals(664.7058823529411, receiveValue!!, 0.00)
        Assert.assertEquals(500.00, sendValue!!, 0.00)
        Assert.assertEquals(3.5, fee, 0.00)

    }

    @Test
    fun convertFromNotBaseToBase() = runBlocking {
        dao.updateUser(User(0, 1))
        currencyViewModel.converterFragmentRepository.manageAction(
            "GBP",
            "EUR",
            500.00,
            ConverterAction.Convert
        )

        val result = dao.getAllConversionsForTesting(1).first()
        val cost = result.cost
        val receiveValue = result.received
        val sendValue = result.send
        val fee = result.fee


        Assert.assertEquals(503.50, cost, 0.00)
        Assert.assertEquals(588.2352941176471, receiveValue!!, 0.00)
        Assert.assertEquals(500.00, sendValue!!, 0.00)
        Assert.assertEquals(3.5, fee, 0.00)

    }

    @After
    fun closeDb() {
        database.close()
    }

}