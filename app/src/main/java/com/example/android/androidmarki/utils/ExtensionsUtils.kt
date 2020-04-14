package com.example.android.androidmarki.utils

import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
@BindingAdapter("afterTextChanged")
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })

}

@BindingAdapter("error")
fun TextInputLayout.error(error: Int?) {
    if (error != 0 && error != null) {
        this.error = context.getString(error)
    } else {
        this.error = null
    }
}


@BindingAdapter("done")
inline fun EditText.done(crossinline function: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_UNSPECIFIED -> {
                function()
            }

        }
        false
    }
}

@BindingAdapter("src")
fun ImageView.src(@DrawableRes drawable: Int) {
    setImageResource(drawable)
}


