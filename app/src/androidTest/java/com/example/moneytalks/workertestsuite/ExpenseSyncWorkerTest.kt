package com.example.moneytalks.notificationsuite

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.example.moneytalks.workers.ExpenseSyncWorker
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import androidx.work.ListenableWorker.Result
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.workertestsuite.FakeApi
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import junit.framework.TestCase.assertTrue
import org.junit.After

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExpenseSyncWorkerTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var context: Context
    private lateinit var fakeApi: FakeApi

    val input = workDataOf(
        "userId" to "u1",
        "groupId" to "g1",
        "amount" to 10.0,
        "description" to "test expense",
        "payersJson" to "[]"
    )

    @Before
    fun setup(){
        context = ApplicationProvider.getApplicationContext<Context>()

        fakeApi = FakeApi()

        WorkManagerTestInitHelper.initializeTestWorkManager(context)

        mockkObject(RetrofitClient)
        every { RetrofitClient.api } returns fakeApi
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
    @Test
    fun succes_returnSucces() = runTest {
        val worker = TestListenableWorkerBuilder<ExpenseSyncWorker>(context)
            .setInputData(input)
            .build()

        val result = worker.doWork()

        assertTrue(result is Result.Success)
    }

    @Test
    fun networkFailure_returnRetry() = runTest {
        fakeApi.shouldThrowNetworkError = true

        val worker = TestListenableWorkerBuilder<ExpenseSyncWorker>(context)
            .setInputData(input)
            .build()

        val result = worker.doWork()

        assertTrue(result is Result.Retry)
    }

    @Test
    fun httpFailure_returnFailure() = runTest {
        fakeApi.shouldThrowHttpException = true

        val worker = TestListenableWorkerBuilder<ExpenseSyncWorker>(context)
            .setInputData(input)
            .build()

        val result = worker.doWork()

        assertTrue(result is Result.Failure)
    }

}