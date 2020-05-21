package com.example.android.androidmarki.ui.home.motionlayout.step1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentMotionLayoutStep1Binding
import com.example.android.androidmarki.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class MotionLayoutStep1Fragment : BaseFragment() {
private lateinit var  binding: FragmentMotionLayoutStep1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMotionLayoutStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

}