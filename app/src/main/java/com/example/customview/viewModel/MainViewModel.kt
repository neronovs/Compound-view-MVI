package com.example.customview.viewModel

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.customview.R
import com.example.customview.model.CardsModel
import com.example.customview.view.ErrorState
import com.example.customview.view.LoadingState
import com.example.customview.view.SuccessState
import com.example.customview.view.ViewStates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainViewModel(
    app: Application,
    val disp: CoroutineDispatcher? = null
) : AndroidViewModel(app),

    IViewModel<ViewStates, RefillIntent> {

    private var initState: Boolean = true

    override val intents: Channel<RefillIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableLiveData<ViewStates>()
    override val state: LiveData<ViewStates>
        get() = _state


    init {
        handleIntent()
    }

    private fun handleIntent() = viewModelScope.launch {
        intents.consumeAsFlow().collect { userIntent ->
            when (userIntent) {
                RefillFields -> refillData()
            }
        }
    }


    private fun refillData() = viewModelScope.launch(disp ?: Dispatchers.Default) {
        try {
            _state.postValue(LoadingState)

            fun stuff() {
                _state.postValue(
                    SuccessState(
                        CardsModel(
                            //Name filling
                            nameText = getString(
                                if (initState) R.string.init_name_text else R.string.refill_name_text
                            ),
                            nameHint = getString(
                                if (initState) R.string.init_name_hint else R.string.refill_name_hint
                            ),
                            nameImage = if (initState) R.mipmap.ic_name else R.mipmap.ic_angela,

                            //Address filling
                            addressText = getString(
                                if (initState) R.string.init_address_text else R.string.refill_address_text
                            ),
                            addressHint = getString(
                                if (initState) R.string.init_address_hint else R.string.refill_address_text
                            ),
                            addressImage = if (initState) R.mipmap.ic_address else R.mipmap.ic_bundestag
                        )
                    )
                )
            }

            disp?.let { stuff() } ?: Executors.newSingleThreadScheduledExecutor()
                .schedule({ stuff() }, 2, TimeUnit.SECONDS)

        } catch (e: Exception) {
            _state.postValue(ErrorState(e))
        }
    }.also { initState = !initState }

    private fun getString(@StringRes textRes: Int) = getApplication<Application>()
        .getString(textRes)

}