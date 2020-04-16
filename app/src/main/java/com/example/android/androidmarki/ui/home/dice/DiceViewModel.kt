package com.example.android.androidmarki.ui.home.dice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.androidmarki.R
import com.example.android.androidmarki.ui.base.BaseViewModel
import java.util.*

class DiceViewModel : BaseViewModel() {
    private val _diceDrawableResource: MutableLiveData<Int> = MutableLiveData(R.drawable.empty_dice)
    val diceDrawableResource: LiveData<Int> = _diceDrawableResource

    fun rollDice() {
        _diceDrawableResource.value = when (Random().nextInt(6) + 1) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }

    }

}
