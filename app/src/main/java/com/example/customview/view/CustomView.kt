package com.example.customview.view

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.customview.R
import com.google.android.material.textfield.TextInputLayout

class CustomView(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {

    private val imageView: ImageView
    private val hint: TextInputLayout
    private val text: EditText

    init {
        inflate(context, R.layout.custom_view, this)

        imageView = findViewById(R.id.image)
        hint = findViewById(R.id.hint)
        text = findViewById(R.id.text)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomView)

        imageView.setImageDrawable(attributes.getDrawable(R.styleable.CustomView_image))

        hint.hint = attributes.getString(R.styleable.CustomView_hint)

        text.setText(attributes.getString(R.styleable.CustomView_text))

        attributes.recycle()
    }

    fun setImage(@DrawableRes image: Int) =
        imageView.setImageResource(image)

    fun setHint(@StringRes txt: Int) =
        context?.getString(txt)?.let { hint.hint = it }

    fun setHint(txt: String) = hint.setHint(txt)

    fun setText(@StringRes txt: Int) =
        context?.getString(txt)?.let { text.setText(it) }

    fun setText(txt: String) = text.setText(txt)

}

