package com.android.currencyAPP.ui

import com.robinhood.spark.SparkAdapter

class SparkViewAdapter : SparkAdapter() {
    private var yData = arrayOf(0.0f, 10.0f, 2.0f, 3.0f, 5.0f, 10.0f, 0.0f, 10.0f, 2.0f, 3.0f, 5.0f)

    fun MyAdapter(yData: Array<Float>) {
        this.yData = yData
    }

    override fun getCount(): Int {
        return yData.size
    }

    override fun getItem(index: Int): Any? {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index]
    }
}