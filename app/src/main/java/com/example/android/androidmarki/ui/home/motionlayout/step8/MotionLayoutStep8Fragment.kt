package com.example.android.androidmarki.ui.home.motionlayout.step8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout

import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentMotionLayoutStep8Binding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.home.HomeActivity
import com.google.android.material.appbar.AppBarLayout

/**
 * A simple [Fragment] subclass.
 */
class MotionLayoutStep8Fragment : BaseFragment() {
    private lateinit var  binding: FragmentMotionLayoutStep8Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as HomeActivity).supportActionBar?.hide()
        // Inflate the layout for this fragment
        binding = FragmentMotionLayoutStep8Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coordinateMotion()

    }
    private fun coordinateMotion() {


        val listener = AppBarLayout.OnOffsetChangedListener { unused, verticalOffset ->
            val seekPosition = -verticalOffset / binding.appbarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }

        binding.appbarLayout.addOnOffsetChangedListener(listener)
    }
}
