package com.example.android.androidmarki.ui.main.reset

import androidx.lifecycle.MutableLiveData
import java.lang.Exception

/**
 * Authentication result : success (user details) or error message.
 */
data class ResetResult(
        val isLoading : Boolean = false,
        val isSuccessful:Boolean = false,
        val exception: Exception?=  null,
        val error: Int? = null
)
