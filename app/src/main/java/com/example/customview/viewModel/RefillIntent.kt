package com.example.customview.viewModel

interface IIntent

sealed class RefillIntent : IIntent {

    object RefillFields : RefillIntent()

}