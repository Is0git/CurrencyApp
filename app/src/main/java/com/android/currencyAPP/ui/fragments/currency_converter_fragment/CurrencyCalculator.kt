package com.android.currencyAPP.ui.fragments.currency_converter_fragment

import javax.inject.Inject
import kotlin.math.absoluteValue

//calculates and returns receive values with fees

open class CurrencyCalculator @Inject constructor() : CurrencyCalculatorListener {


    override fun calculateReceiveFromBaseCurrency(
        valueFrom: Double,
        currencyRatio: Double
    ) = valueFrom * currencyRatio


    override suspend fun calculateReceiveWithoutBaseCurrency(
        valueFrom: Double,
        fromValueCurrency: Double,
        toValueCurrency: suspend () -> Double
    ): Double = (valueFrom / fromValueCurrency) * toValueCurrency()


    override fun calculateFee(valueFrom: Double, feePercentage: Double) = valueFrom * feePercentage


    override fun calculateTotalCostOfSend(valueFrom: Double, feePercentage: Double) =
        valueFrom + calculateFee(valueFrom, feePercentage)

    override fun calculateSendRemainder(balance: Double, sendValue: Double) =
        (balance - sendValue).absoluteValue


}