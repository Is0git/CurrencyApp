package com.android.currencyAPP.util

sealed class ConverterState {
    data class OnConvertFinish(
        var receiveValue: Double,
        var sendValue: Double,
        var totalCost: Double,
        var feeCost: Double,
        var fromType: String,
        var toType: String
    ) : ConverterState()

    object OnConvertStart : ConverterState()

    object OnConvertFailed : ConverterState()

    object OnBalanceZero : ConverterState()

    object Completed : ConverterState()

//    data class OnPeek(var receiverValue: Double) : ConverterState()
}