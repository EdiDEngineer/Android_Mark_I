package com.example.android.androidmarki.ui.main.signUp

import androidx.lifecycle.MutableLiveData
import java.lang.Exception

/**
 * Authentication result : success (user details) or error message.
 */
data class SignUpResult(
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val exception: Exception? = null,
    val error: Int? = null
)
