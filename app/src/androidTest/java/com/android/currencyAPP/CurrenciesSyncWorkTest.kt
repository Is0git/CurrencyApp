package com.android.currencyAPP


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.work.*
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.android.currencyAPP.ui.CURRENCIES_SYNC_WORK
import com.android.currencyAPP.util.CurrenciesSyncWorker
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
class CurrenciesSyncWorkTest {
    lateinit var workManger: WorkManager
    lateinit var context: Context
    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<Application>()
        val config = Configuration.Builder()
            // Set log level to Log.DEBUG to make it easier to debug
            .setMinimumLoggingLevel(Log.DEBUG)
            // Use a SynchronousExecutor here to make it easier to write tests
            .setExecutor(SynchronousExecutor())
            .build()
        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
        workManger = WorkManager.getInstance(context)
    }

    @Test
    @Throws(Exception::class)
    fun testCurrenciesSyncPeriodicWork() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val work = PeriodicWorkRequestBuilder<CurrenciesSyncWorker>(5, TimeUnit.MINUTES).addTag(
            CURRENCIES_SYNC_WORK
        ).setConstraints(constraints).build()


        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)
        // Enqueue and wait for result.
        workManger.enqueue(work).result.get()
        // Tells the testing framework the period delay is met
        testDriver?.setPeriodDelayMet(work.id)
        // Get WorkInfo and outputData
        val workInfo = workManger.getWorkInfoById(work.id).get()
        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))
    }
}