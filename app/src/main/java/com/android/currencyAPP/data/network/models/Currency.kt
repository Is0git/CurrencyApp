package com.android.currencyAPP.data.network.models

import com.squareup.moshi.Json


data class Currency(
    @Json(name = "rates") val rates: Map<String, Double>?, val base: String?,
    val date: String?
)


