package com.example.android.androidmarki.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BaseViewModelFactory(private val baseViewModel: BaseViewModel) :
    ViewModelProvider.AndroidViewModelFactory(baseViewModel.getApplication()) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(baseViewModel::class.java)) {
            return baseViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}