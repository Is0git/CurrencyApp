package com.android.currencyAPP.ui.fragments.currency_converter_fragment

interface CurrencyCalculatorListener {

    //put feePercentage in methods instead of having general field, just in case program has different fees for every currency and have various constraints

    fun calculateReceiveFromBaseCurrency(valueFrom: Double, currencyRatio: Double): Double

    suspend fun calculateReceiveWithoutBaseCurrency(
        valueFrom: Double,
        fromValueCurrency: Double,
        toValueCurrency: suspend () -> Double
    ): Double

    fun calculateFee(valueFrom: Double, feePercentage: Double): Double

    fun calculateTotalCostOfSend(valueFrom: Double, feePercentage: Double): Double

    //calculates cost,  when user doesn't have enough money in balance in relation to send value
    fun calculateSendRemainder(balance: Double, sendValue: Double): Double

}