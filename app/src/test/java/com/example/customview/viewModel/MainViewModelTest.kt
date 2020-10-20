package com.example.customview.viewModel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.customview.BaseTest
import com.example.customview.model.CardsModel
import com.example.customview.view.LoadingState
import com.example.customview.view.SuccessState
import io.mockk.*
import junit.framework.TestCase.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest : BaseTest() {

    private lateinit var viewModel: MainViewModel

    private val app: Application = mockkClass(Application::class)

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    internal fun before() {
        viewModel = MainViewModel(app, TestCoroutineDispatcher())

        coEvery { app.getString(any()) } returns dummyString
    }

    @Test
    internal fun `stability handle intent test`() {
        TestCoroutineScope().launch(Dispatchers.Main) {  // Will be launched in the mainThreadSurrogate dispatcher
            assertNotNull(viewModel.intents.send(RefillFields))
        }
    }

    @Test
    internal fun `state LiveData test`() {
        viewModel.state.observeForever {
            if (it is SuccessState) {
                assertEquals(CardsModel().javaClass, it.res.javaClass)
                assertEquals(dummyString, it.res.addressHint)
                assertEquals(dummyString, it.res.addressText)
                assertEquals(dummyString, it.res.nameHint)
                assertEquals(dummyString, it.res.nameText)
            }
        }

        TestCoroutineScope().launch(Dispatchers.Main) {
            viewModel.intents.send(RefillFields)
        }

        assertNotNull(viewModel.state)
        assertNotNull(viewModel.state.value)
        assertFalse(viewModel.state.value?.isLoading()!!)
    }

    @Test
    internal fun `application-getString() verification test`() {
        viewModel.state.observeForever {}

        TestCoroutineScope().launch(Dispatchers.Main) {
            viewModel.intents.send(RefillFields)
        }

        coVerify(exactly = 4) {
            app.getString(any())
        }

        confirmVerified(app)
    }

}