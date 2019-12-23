package com.android.currencyAPP.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.currencyAPP.di.activity.MainActivityScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@MainActivityScope
class MainActivityViewModel @Inject constructor(val repo: MainActivityRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repo.updateCurrencyDb()
        }
    }
    var loadingStates = repo.loadingStatesLiveData
    fun addFees(userId: Int, feesNumber: Int) =
        viewModelScope.launch(Dispatchers.IO) { repo.addFees(userId, feesNumber) }

    var balancesLiveData = repo.getBalances()
}