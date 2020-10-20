package com.example.customview.view

import com.example.customview.model.CardsModel

interface IState

sealed class ViewStates : IState {

    object LoadingState : ViewStates()

    data class SuccessState(val res: CardsModel) : ViewStates()

    data class ErrorState(val err: Throwable) : ViewStates()

    fun isLoading() = this is LoadingState

}