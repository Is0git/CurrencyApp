package com.android.currencyAPP.util.data_binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.currencyAPP.R

object TypeResolver {
    @JvmStatic
    @BindingAdapter("app:resolveImage", "app:resolveText")
    fun resolveType(imageView: ImageView, type: String, text: TextView) {
        when (type) {
            "CAD" -> setCardProperty(text, imageView, R.drawable.dollar_icon, R.string.CAD)
            "HKD" -> setCardProperty(text, imageView, R.drawable.dollar_icon, R.string.HKD)
            "ISK" -> setCardProperty(text, imageView, R.drawable.krona_icon, R.string.ISK)
            "PHP" -> setCardProperty(text, imageView, R.drawable.peso_icon, R.string.PHP)
            "DKK" -> setCardProperty(text, imageView, R.drawable.krona_icon, R.string.DKK)
            "HUF" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.HUF)
            "CZK" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.CZK)
            "AUD" -> setCardProperty(text, imageView, R.drawable.dollar_icon, R.string.AUD)
            "RON" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.RON)
            "SEK" -> setCardProperty(text, imageView, R.drawable.krona_icon, R.string.SEK)
            "IDR" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.IDR)
            "INR" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.INR)
            "BRL" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.BRL)
            "RUB" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.RUB)
            "HRK" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.HRK)
            "JPY" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.JPY)
            "THB" -> setCardProperty(text, imageView, R.drawable.baht_icon, R.string.THB)
            "CHF" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.CHF)
            "SGD" -> setCardProperty(text, imageView, R.drawable.dollar_icon, R.string.SGD)
            "PLN" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.PLN)
            "BGN" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.BGN)
            "TRY" -> setCardProperty(text, imageView, R.drawable.lira_icon, R.string.TRY)
            "CNY" -> setCardProperty(text, imageView, R.drawable.krona_icon, R.string.CNY)
            "NOK" -> setCardProperty(text, imageView, R.drawable.krona_icon, R.string.NOK)
            "NZD" -> setCardProperty(text, imageView, R.drawable.dollar_icon, R.string.NZD)
            "ZAR" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.ZAR)
            "USD" -> setCardProperty(text, imageView, R.drawable.dollar_icon, R.string.USD)
            "MXN" -> setCardProperty(text, imageView, R.drawable.peso_icon, R.string.MXN)
            "ILS" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.ILS)
            "KRW" -> setCardProperty(text, imageView, R.drawable.won_icon, R.string.KRW)
            "MYR" -> setCardProperty(text, imageView, R.drawable.money_icon, R.string.MYR)
            "GBP" -> setCardProperty(text, imageView, R.drawable.pound_icon, R.string.GBP)

        }
    }

    private fun setCardProperty(
        text: TextView,
        imageView: ImageView,
        drawableId: Int,
        stringId: Int
    ) {
        text.setText(stringId)
        imageView.setImageResource(drawableId)
    }
}