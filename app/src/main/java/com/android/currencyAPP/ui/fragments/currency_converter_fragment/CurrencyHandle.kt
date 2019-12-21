package com.android.currencyAPP.ui.fragments.currency_converter_fragment

abstract class CurrencyHandle : CurrencyCalculator() {

    var sendValue = 0.00 // value that user actually typed
    var receiveValue = 0.00 // actual value which user will receive
    var totalCost = 0.00 //value of total cost with fees, constraints
    var feePercentage = BASE_PERCENTAGE
    var fee = 0.00

    suspend fun calculate(
        isFree: Boolean,
        typedValue: Double?,
        balance: Double?,
        fromCurrencyType: String?,
        toCurrencyType: String?,
        toTypeRatio: Double?,
        fromTypeRatio: Double?
    ) {
        feePercentage = if (isFree) 0.0 else BASE_PERCENTAGE
        sendValue = typedValue!!
        totalCost = calculateTotalCost(typedValue) { calculateFee(it, feePercentage) }
        handleBelowZeroBalance(totalCost, balance!!, feePercentage)
        receiveValue = when (fromCurrencyType) {
            BASE_CURRENCY -> {                                      // from base_currency
                calculateReceiveFromBaseCurrency(
                    sendValue,
                    toTypeRatio!!
                )
            }
            else -> {                                                        // from other than base
                calculateReceiveWithoutBaseCurrency(
                    sendValue,
                    fromTypeRatio!!
                ) { if (toCurrencyType == BASE_CURRENCY) 1.0 else toTypeRatio!! } // if we change to base currency(USD -> EUR) second ratio is not needed
            }
        }
    }

    private inline fun calculateTotalCost(
        value: Double,
        fee: (typedValue: Double) -> Double
    ): Double {
        return value + fee(value).also { this.fee = it }
    }


    private fun handleBelowZeroBalance(totalCost: Double, balance: Double, feePercentage: Double) {
        if (totalCost > balance) {   //check if balance is lower than send total value
            sendValue = calculateNextValue(
                balance,
                feePercentage
            )                                                                                       //calculate min available value (y = x + fee%*x) y - desired value
            this.totalCost =
                balance                                                     //cost is going to be leftovers in balance
            this.fee = calculateFee(
                sendValue,
                feePercentage
            )                                        // need to calculate new fee since send we send different value
        }
    }

    private fun calculateNextValue(balance: Double, feePercentage: Double) =
        balance / (1 + feePercentage)

}