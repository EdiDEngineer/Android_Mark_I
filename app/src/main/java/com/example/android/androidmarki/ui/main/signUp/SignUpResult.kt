package com.example.android.androidmarki.ui.main.signUp

import androidx.lifecycle.MutableLiveData
import java.lang.Exception

/**
 * Authentication result : success (user details) or error message.
 */
data class SignUpResult(
    var isSuccessful: Boolean = false,
    val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(),
    var exception: Exception? = null,
    var error: Int? = null
)
