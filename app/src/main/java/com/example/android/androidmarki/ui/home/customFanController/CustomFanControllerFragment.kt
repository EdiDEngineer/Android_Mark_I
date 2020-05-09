package com.example.android.androidmarki.ui.home.customFanController

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.androidmarki.databinding.FragmentCustomFanControllerBinding
import com.example.android.androidmarki.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class CustomFanControllerFragment : BaseFragment() {

    private lateinit var binding: FragmentCustomFanControllerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomFanControllerBinding.inflate(inflater, container, false)
        return binding.root
    }

}
