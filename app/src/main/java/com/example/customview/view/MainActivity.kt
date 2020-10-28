package com.example.customview.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.customview.R
import com.example.customview.model.CardsModel
import com.example.customview.viewModel.MainViewModel
import com.example.customview.viewModel.RefillFields
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeLiveData()

        setContentView(R.layout.activity_main)

        buttonRefillFields?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.intents.send(RefillFields)
            }
        }
    }

    private fun subscribeLiveData() = viewModel.state.observe(this, { viewState ->
        progressBar?.isVisible = viewState.isLoading()
        buttonRefillFields?.isEnabled = !viewState.isLoading()

        when (viewState) {
            is SuccessState -> refillFields(viewState.res)
            is ErrorState -> AlertDialog.Builder(this)
                .setTitle("Error!")
                .show()
            else -> Unit
        }
    })

    private fun refillFields(res: CardsModel) = res.run {
        nameField?.run {
            nameImage?.let { setImage(it) }
            nameText?.let { setText(it) }
            nameHint?.let { setHint(it) }
        }

        addressField?.run {
            addressImage?.let { setImage(it) }
            addressText?.let { setText(it) }
            addressHint?.let { setHint(it) }
        }
    }

}