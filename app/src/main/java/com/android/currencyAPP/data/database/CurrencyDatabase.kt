package com.android.currencyAPP.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.currencyAPP.data.database.dao.CurrencyDao
import com.android.currencyAPP.data.database.entities.Balance
import com.android.currencyAPP.data.database.entities.Conversion
import com.android.currencyAPP.data.database.entities.Currencies
import com.android.currencyAPP.data.database.entities.User


@Database(
    entities = [User::class, Currencies::class, Balance::class, Conversion::class],
    version = 1,
    exportSchema = true
)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

}