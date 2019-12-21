package com.android.currencyAPP.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversion_table")
data class Conversion(
    @PrimaryKey(autoGenerate = true) val id: Int, val fromType: String?,
    val toType: String?,
    val dateLong: Long?,
    val send: Double?,
    val received: Double?,
    val fee: Double,
    val cost: Double,
    val userId: Int
)