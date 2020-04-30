package com.example.android.androidmarki.ui.main.verify

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.androidmarki.R

data class VerifyUIData(
    val phoneNumber: MutableLiveData<String> = MutableLiveData<String>(),
    val code: MutableLiveData<String> = MutableLiveData<String>(),
    val phoneNumberError: LiveData<Int> = Transformations.map(phoneNumber) {
        if (!it.startsWith("+")||it.length<12) R.string.invalid_number else null
    },
    val codeError: LiveData<Int> = Transformations.map(code) {
        if (it.length!=6) R.string.invalid_code else null
    },
    var isDataValid:Boolean = false,
    val isCode : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    )