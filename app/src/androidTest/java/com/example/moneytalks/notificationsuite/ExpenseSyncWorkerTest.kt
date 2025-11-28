package com.example.moneytalks.notificationsuite

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.example.moneytalks.workers.ExpenseSyncWorker
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.moneytalks.apisetup.MoneyTalksInterface
import com.example.moneytalks.apisetup.RetrofitClient

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@RunWith(AndroidJUnit4::class)
class ExpenseSyncWorkerTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
    }

    @Test
    fun success_returnSuccess() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val mockApi = mock<MoneyTalksInterface> {
            onBlocking { createExpense(any(), any())} doReturn Unit
        }

        val mockRetrofitClient = mock<RetrofitClient> {
            on { api } doReturn mockApi
        }

        val input = workDataOf(
            "userId" to "u1",
            "groupId" to "g1",
            "amount" to 10.0,
            "description" to "test expense"
        )

        val workerFactory = object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker? {
                return ExpenseSyncWorker(
                    appContext = appContext,
                    workerParameters = workerParameters,
                    retrofitClient = mockRetrofitClient
                )
            }
        }

        val worker = TestListenableWorkerBuilder<ExpenseSyncWorker>(context)
            .setWorkerFactory(workerFactory)
            .setInputData(input)
            .build()

        val result = worker.doWork()

        assertEquals(Result.success(), result)
    }
}