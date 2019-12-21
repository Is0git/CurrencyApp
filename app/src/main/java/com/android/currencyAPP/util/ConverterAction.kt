package com.android.currencyAPP.util

sealed class ConverterAction {
    object Convert : ConverterAction()

    object PeekReceive : ConverterAction()
}