package com.example.customview.viewModel

import androidx.lifecycle.LiveData
import com.example.customview.view.IState
import kotlinx.coroutines.channels.Channel

interface IViewModel<S: IState, I: IIntent> {
    val intents: Channel<I>
    val state: LiveData<S>
}