package com.android.currencyAPP.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies_table")
data class Currencies(@PrimaryKey val type: String, val value: Double) {
}