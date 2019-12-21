package com.android.currencyAPP.data.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.android.currencyAPP.data.database.entities.Balance
import com.android.currencyAPP.data.database.entities.Conversion
import com.android.currencyAPP.data.database.entities.Currencies
import com.android.currencyAPP.data.database.entities.User

@Dao
interface CurrencyDao {

    //    @Query("SELECT * FROM user_table")
//    fun getUserData() : LiveData<User>
//
//    @Query("SELECT * FROM rates_table")
//    suspend fun getRates() : Rates
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun updateRates()
    @Query("SELECT value FROM currencies_table WHERE type == :type ")
    suspend fun getCurrency(type: String): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currencies)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalanceCurrencies(values: Balance)

    @Query("SELECT * FROM balance_table ORDER BY type ASC")
    fun getBalances(): LiveData<List<Balance>>

    @Query("UPDATE balance_table SET balanceValue = balanceValue + :value WHERE type == :type")
    suspend fun updateValue(type: String, value: Double)

    @Query("UPDATE balance_table SET balanceValue = balanceValue - :value WHERE type == :type")
    suspend fun decreaseValue(type: String, value: Double)

    @Query("SELECT balanceValue FROM balance_table WHERE type == :type")
    suspend fun getBalance(type: String): Double

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT feesLeft from USER_TABLE WHERE id == :userId")
    suspend fun getUserFees(userId: Int): Int

    @Query("SELECT * FROM USER_TABLE WHERE id == :id")
    fun getUser(id: Int): LiveData<User>

    @Query("UPDATE user_table SET feesLeft = feesLeft - 1 WHERE id == :id")
    suspend fun decreaseFee(id: Int)

    @Query("UPDATE user_table SET feesLeft = feesLeft + :feesNumber WHERE id == :id")
    suspend fun addFees(id: Int, feesNumber: Int)

    @Insert
    suspend fun addConversion(conversion: Conversion)

    @Query("SELECT * FROM conversion_table WHERE userId == :id ORDER BY dateLong DESC")
    fun getAllConversions(id: Int): DataSource.Factory<Int, Conversion>

    @Query("SELECT * FROM conversion_table WHERE userId == :id ORDER BY dateLong DESC")
    fun getAllConversionsForTesting(id: Int): List<Conversion>

    @Query("SELECT count(*) FROM balance_table")

    suspend fun getBalancesCount(): Int

    @Update
    suspend fun updateUser(user: User)
}