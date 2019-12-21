package com.android.currencyAPP.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object FocusHelper {

    fun hideKeyboard(context: Context, mView: View) {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mView.windowToken, 0)
    }
}