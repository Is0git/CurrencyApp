package com.android.currencyAPP.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balance_table")
data class Balance(@PrimaryKey val type: String, val balanceValue: Double = 0.0)
