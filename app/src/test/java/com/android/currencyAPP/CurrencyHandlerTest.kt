package com.android.currencyAPP

import com.android.currencyAPP.ui.fragments.currency_converter_fragment.CurrencyHandle
import org.junit.Assert
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CurrencyHandlerTest {

    lateinit var currencyHandle: CurrencyHandle

    @Before
    fun init() {
        currencyHandle = object : CurrencyHandle() {
            init {
                feePercentage = 0.007
            }
        }

    }

    @Test
    fun regularFreeConvertFromBase() = runBlocking {
        currencyHandle.calculate(true, 500.00, 1000.00, "EUR", "USD", 1.13, 1.00)
        Assert.assertEquals(500.00, currencyHandle.totalCost, 0.00)
        Assert.assertEquals(565.00, currencyHandle.receiveValue, 0.00)
        Assert.assertEquals(500.00, currencyHandle.sendValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.fee, 0.00)
    }

    @Test
    fun regularConvertFromBase() = runBlocking {
        currencyHandle.calculate(false, 500.00, 1000.00, "EUR", "USD", 1.13, 1.00)
        Assert.assertEquals(503.5, currencyHandle.totalCost, 0.00)
        Assert.assertEquals(565.00, currencyHandle.receiveValue, 0.00)
        Assert.assertEquals(500.00, currencyHandle.sendValue, 0.00)
        Assert.assertEquals(3.5, currencyHandle.fee, 0.00)
    }


    @Test
    fun regularFreeConvertFromNotBase() = runBlocking {
        currencyHandle.calculate(true, 500.00, 1000.00, "GBP", "USD", 1.13, 0.85)
        Assert.assertEquals(500.00, currencyHandle.totalCost, 0.00)
        Assert.assertEquals(664.7058823529411, currencyHandle.receiveValue, 0.00)
        Assert.assertEquals(500.00, currencyHandle.sendValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.fee, 0.00)
    }


    @Test
    fun regularConvertFromNotBase() = runBlocking {
        currencyHandle.calculate(false, 500.00, 1000.00, "GBP", "USD", 1.13, 0.85)
        Assert.assertEquals(503.50, currencyHandle.totalCost, 0.00)
        Assert.assertEquals(664.7058823529411, currencyHandle.receiveValue, 0.00)
        Assert.assertEquals(500.00, currencyHandle.sendValue, 0.00)
        Assert.assertEquals(3.5, currencyHandle.fee, 0.00)
    }


    @Test
    fun regularFreeConvertFromBaseWhenZero() = runBlocking {
        currencyHandle.calculate(true, 0.00, 1000.00, "EUR", "USD", 1.13, 1.00)
        Assert.assertEquals(0.00, currencyHandle.totalCost, 0.00)
        Assert.assertEquals(0.00, currencyHandle.receiveValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.sendValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.fee, 0.00)
    }

    @Test
    fun regularConvertFromBaseWhenZero() = runBlocking {
        currencyHandle.calculate(false, 0.00, 1000.00, "EUR", "USD", 1.13, 1.00)
        Assert.assertEquals(0.00, currencyHandle.totalCost, 0.00)
        Assert.assertEquals(0.00, currencyHandle.receiveValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.sendValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.fee, 0.00)
    }


    @Test
    fun negativeBalanceFreeConvertFromBase() = runBlocking {
        currencyHandle.calculate(true, 500.00, 458.00, "EUR", "USD", 1.13, 1.00)
        Assert.assertEquals(currencyHandle.totalCost, 458.00, 0.00)
        Assert.assertEquals(currencyHandle.receiveValue, 517.54, 0.00)
        Assert.assertEquals(currencyHandle.sendValue, 458.00, 0.00)
        Assert.assertEquals(currencyHandle.fee, 0.00, 0.00)
    }

    @Test
    fun negativeBalanceConvertFromBase() = runBlocking {
        currencyHandle.calculate(false, 500.00, 458.00, "EUR", "USD", 1.130, 1.00)
        Assert.assertEquals(currencyHandle.totalCost,458.00 , 0.00)
        Assert.assertEquals(currencyHandle.receiveValue, 513.9424031777558, 0.00)
        Assert.assertEquals(currencyHandle.sendValue,454.81628599801394 , 0.00)
        Assert.assertEquals(currencyHandle.fee,3.1837140019860977 , 0.00)
    }

    //
//
    @Test
    fun negativeBalanceFreeConvertNotFromBase() = runBlocking {
        currencyHandle.calculate(true, 500.00, 458.00, "GBP", "USD", 1.13, 0.85)
        Assert.assertEquals(458.00, currencyHandle.totalCost, 0.00)
        Assert.assertEquals(608.8705882352941, currencyHandle.receiveValue, 0.00)
        Assert.assertEquals(458.00, currencyHandle.sendValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.fee, 0.00)
    }

    @Test
    fun negativeBalanceConvertNotFromBase() = runBlocking {
        currencyHandle.calculate(false, 500.00, 458.00, "GBP", "USD", 1.13, 0.85)
        Assert.assertEquals(458.00, currencyHandle.totalCost, 0.00)
        Assert.assertEquals(604.638121385595, currencyHandle.receiveValue, 0.00)
        Assert.assertEquals(454.81628599801394, currencyHandle.sendValue, 0.00)
        Assert.assertEquals(3.1837140019860977, currencyHandle.fee, 0.00)
    }

    @Test
    fun negativeBalanceConvertNotFromBaseWhenZero() = runBlocking {
        currencyHandle.calculate(false, 0.00, 458.00, "GBP", "USD", 1.13, 0.85)
        Assert.assertEquals(0.00, currencyHandle.totalCost, 0.00)
        Assert.assertEquals(0.00, currencyHandle.receiveValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.sendValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.fee, 0.00)
    }

    @Test
    fun negativeBalanceFreeConvertNotFromBaseWhenZero() = runBlocking {
        currencyHandle.calculate(false, 0.00, 458.00, "GBP", "USD", 1.13, 0.85)
        Assert.assertEquals(0.00, currencyHandle.totalCost, 0.00)
        Assert.assertEquals(0.00, currencyHandle.receiveValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.sendValue, 0.00)
        Assert.assertEquals(0.00, currencyHandle.fee, 0.00)
    }

}