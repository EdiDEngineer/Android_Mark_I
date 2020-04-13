package com.example.android.androidmarki.ui.main.login

import androidx.lifecycle.MutableLiveData
import java.lang.Exception

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
        val isLoading : MutableLiveData<Boolean> = MutableLiveData<Boolean>(),
        var isSuccessful  : Boolean = false,
        var exception: Exception? = null,
        var error: Int? = null
)
