package com.example.android.androidmarki.ui.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.androidmarki.data.remote.firebase.FirebaseUserAuthLiveData
import com.example.android.androidmarki.ui.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val userLiveData = FirebaseUserAuthLiveData()
}