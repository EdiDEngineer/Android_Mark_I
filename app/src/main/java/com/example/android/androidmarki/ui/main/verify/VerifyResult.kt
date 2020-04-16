package com.example.android.androidmarki.ui.main.verify

import androidx.lifecycle.MutableLiveData
import java.lang.Exception

/**
 * Authentication result : success (user details) or error message.
 */
data class VerifyResult(
        var isSuccessful :Boolean= false,
        val isLoading : Boolean = false,
        val isSignOut  :Boolean= false,
        var exception: Exception?= null,
        var error: Int? = null
)
