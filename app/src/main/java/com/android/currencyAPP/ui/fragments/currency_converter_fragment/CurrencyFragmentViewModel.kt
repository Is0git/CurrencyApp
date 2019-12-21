package com.android.currencyAPP.ui.fragments.currency_converter_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.currencyAPP.data.database.entities.Conversion
import com.android.currencyAPP.di.activity.currency_fragment.CurrencyFragmentScope
import com.android.currencyAPP.util.ConverterAction
import com.android.currencyAPP.util.ConverterState
import kotlinx.coroutines.launch
import javax.inject.Inject

@CurrencyFragmentScope
class CurrencyFragmentViewModel @Inject constructor(val converterFragmentRepository: ConverterFragmentRepository) :
    ViewModel() {

    var convertStates: LiveData<ConverterState> = converterFragmentRepository.statesLiveData

    var userLiveData = converterFragmentRepository.userLiveData

    var conversionList = converterFragmentRepository.conversionsList

    var conversionResult: LiveData<Conversion> = converterFragmentRepository.conversionResult

    fun resetConvertState() {
        converterFragmentRepository.resetConvertState()
    }

    fun resetValues() {
        converterFragmentRepository.resetValues()
    }

    fun manageAction(
        fromCurrencyType: String,
        toCurrencyType: String,
        valueFrom: Double,
        action: ConverterAction
    ) {
        viewModelScope.launch {
            converterFragmentRepository.manageAction(
                fromCurrencyType,
                toCurrencyType,
                valueFrom,
                action
            )
        }
    }
}