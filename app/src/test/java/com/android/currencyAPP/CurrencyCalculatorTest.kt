package com.android.currencyAPP

import com.android.currencyAPP.ui.fragments.currency_converter_fragment.CurrencyCalculator
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test


class CurrencyCalculatorTest {

    private var currencyHandler = CurrencyCalculator()

    @Test
    fun calculateBaseToAnyWithoutFee() {
        val result = currencyHandler.calculateReceiveFromBaseCurrency(325.0, 1.103)
        Assert.assertEquals(358.47499999999997,result, 0.00)
    }

    @Test
    fun calculateWithoutBaseWithoutFee() = runBlocking {
        val result = currencyHandler.calculateReceiveWithoutBaseCurrency(325.0, 1.103) { 1.00 }
        Assert.assertEquals(294.65095194922935, result, 0.00)
    }


    @Test
    fun calculateFee() {
        val feeResult = currencyHandler.calculateFee(100.0, 0.007)
        Assert.assertEquals(0.7000000000000001, feeResult, 0.00)
    }

    @Test
    fun calculateTotalCost() {
        val totalCost = currencyHandler.calculateTotalCostOfSend(100.0, 0.007)
        Assert.assertEquals(100.70, totalCost, 0.00)
    }

    @Test
    fun calculateTotalCostWhenFeeIsZero() {
        val totalCost = currencyHandler.calculateTotalCostOfSend(100.0, 0.0)
        Assert.assertEquals(100.0, totalCost, 0.00)
    }

    @Test
    fun calculateWhenSenderValueIsHigherThanBalance() {
        val remainder = currencyHandler.calculateSendRemainder(125.0, 135.0)
        Assert.assertEquals(10.0, remainder, 0.00)
    }

}