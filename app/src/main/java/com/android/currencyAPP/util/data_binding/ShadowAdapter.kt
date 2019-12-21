package com.android.currencyAPP.util.data_binding

import android.widget.TextView
import androidx.databinding.BindingAdapter


import com.android.currencyAPP.R

object ShadowAdapter {

    @JvmStatic
    @BindingAdapter("app:shadowAdapter")

    fun shadowAdapter(textView: TextView, value: Double) {

        if (value == 0.0) {
            textView.setShadowLayer(
                3f,
                0f,
                15f,
                textView.resources.getColor(R.color.colorSecondaryVariant)
            )
        } else if (value > 0.0) {
            textView.setShadowLayer(
                3f,
                0f,
                10f,
                textView.resources.getColor(R.color.colorSecondaryVariant)
            )
        }
    }
}