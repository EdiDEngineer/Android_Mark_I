package com.example.android.androidmarki.ui.main.reset

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.androidmarki.R

data class ResetUIData(
    val username: MutableLiveData<String> = MutableLiveData<String>(),
    val usernameError: LiveData<Int> = Transformations.map(username) {
        if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) R.string.invalid_username else 0
    }
)