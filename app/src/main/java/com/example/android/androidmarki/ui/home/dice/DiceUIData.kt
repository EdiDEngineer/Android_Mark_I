package com.example.android.androidmarki.ui.home.dice

import androidx.lifecycle.MutableLiveData
import com.example.android.androidmarki.R

data class DiceUIData(
    val drawableResource: MutableLiveData<Int> = MutableLiveData(R.drawable.empty_dice)

)