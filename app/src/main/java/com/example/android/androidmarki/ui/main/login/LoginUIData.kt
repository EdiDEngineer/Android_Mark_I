package com.example.android.androidmarki.ui.main.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.androidmarki.R

data class LoginUIData(
    val username: MutableLiveData<String> = MutableLiveData<String>(),
    val password: MutableLiveData<String> = MutableLiveData<String>(),
    val usernameError: LiveData<Int> = Transformations.map(username) {
        if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) R.string.invalid_username else 0
    },
    val passwordError: LiveData<Int> = Transformations.map(password) {
        if (it.length <= 5) R.string.invalid_password else 0
    },
    var isDataValid: Boolean = false
)