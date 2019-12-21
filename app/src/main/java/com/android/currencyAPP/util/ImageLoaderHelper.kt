package com.android.currencyAPP.util

import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageLoaderHelper {

    fun loadFromDrawableId(id: Int, view: ImageView) {
        Glide.with(view.context).load(id).centerCrop().into(view)
    }
}