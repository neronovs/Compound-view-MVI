package com.example.customview

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class BaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    protected val dummyString = "dummyString"

    @Before
    internal fun beforeFunc() {
        // Set LiveData Executor.
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun postToMainThread(runnable: Runnable) = runnable.run()
            override fun isMainThread(): Boolean = true
        })
    }

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @After
    internal fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }


}