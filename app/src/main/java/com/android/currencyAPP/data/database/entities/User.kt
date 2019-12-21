package com.android.currencyAPP.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    val feesLeft: Int?, @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
}