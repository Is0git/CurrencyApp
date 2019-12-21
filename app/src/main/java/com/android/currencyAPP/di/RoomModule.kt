package com.android.currencyAPP.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.currencyAPP.data.database.CurrencyDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RoomModule {

    @JvmStatic
    @Provides
    @Singleton
    fun getDatabase(application: Application): CurrencyDatabase = Room.databaseBuilder(
        application,
        CurrencyDatabase::class.java,
        "currency_database"
    ).fallbackToDestructiveMigration().addCallback(callback).build()
}

val callback = object : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        Log.d("TAG", "SS1")
        db.execSQL("INSERT INTO user_table(feesLeft) VALUES (3)")
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        Log.d("TAG", "SS2")

    }

    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
        super.onDestructiveMigration(db)
        Log.d("TAG", "SS3")
    }
}