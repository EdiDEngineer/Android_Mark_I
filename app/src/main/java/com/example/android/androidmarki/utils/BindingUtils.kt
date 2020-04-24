package com.example.android.androidmarki.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.local.entity.SleepTrackerNight
import com.google.android.material.textfield.TextInputLayout

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
@BindingAdapter("afterTextChanged")
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })

}

@BindingAdapter("error")
fun TextInputLayout.error(error: Int?) {
    if (error == 0 || error == null) {
        this.error = null
    } else {
        this.error = context.getString(error)
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

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepTrackerNight?) {
    item?.let {
        setImageResource(when (item.sleepQuality) {
            0 -> R.drawable.sleep_tracker_ic_sleep_0
            1 -> R.drawable.sleep_tracker_ic_sleep_1
            2 -> R.drawable.sleep_tracker_ic_sleep_2
            3 -> R.drawable.sleep_tracker_ic_sleep_3
            4 -> R.drawable.sleep_tracker_ic_sleep_4
            5 -> R.drawable.sleep_tracker_ic_sleep_5
            else -> R.drawable.sleep_tracker_ic_sleep_active
        })
    }
}

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepTrackerNight?) {
    item?.let {
        text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, context.resources)
    }
}

@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(item: SleepTrackerNight?) {
    item?.let {
        text = convertNumericQualityToString(item.sleepQuality, context.resources)
    }
}

