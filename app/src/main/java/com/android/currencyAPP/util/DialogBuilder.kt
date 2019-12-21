package com.android.currencyAPP.util

import android.content.Context
import android.text.Spanned
import com.android.currencyAPP.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogBuilder {
    fun showDialog(context: Context, title: String, message: Spanned) {
        MaterialAlertDialogBuilder(context, R.style.convertMaterialDialogStyle)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton("OK", null)
            .show()
    }
}