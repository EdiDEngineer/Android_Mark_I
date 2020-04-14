package com.example.android.androidmarki.ui.home.dice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.android.androidmarki.databinding.FragmentDiceBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory

class DiceFragment : BaseFragment() {

    private lateinit var binding: FragmentDiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiceBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProvider(
                this@DiceFragment, BaseViewModelFactory(
                    DiceViewModel()
                )
            ).get(
                DiceViewModel::class.java
            )
            lifecycleOwner = viewLifecycleOwner
        }
        snackView = binding.root
        return snackView
    }


}
