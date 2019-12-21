package com.android.currencyAPP.data.network

import com.android.currencyAPP.data.network.models.Currency
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyService {

    @GET("latest")
    suspend fun getCurrencies(): Response<Currency>
}