package com.example.android.androidmarki.ui.home.tickCross

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentTickCrossBinding
import com.example.android.androidmarki.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class TickCrossFragment : BaseFragment() {
    private lateinit var binding: FragmentTickCrossBinding
    private var tick = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentTickCrossBinding.inflate(inflater, container, false).apply {
            tickCrossFragment = this@TickCrossFragment

        }

        return binding.root
    }

    fun animate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val drawable: AnimatedVectorDrawable =
                if (tick) (context?.getDrawable(R.drawable.avd_tick_to_cross) as AnimatedVectorDrawable) else (context?.getDrawable(
                    R.drawable.avd_cross_to_tick
                ) as AnimatedVectorDrawable)
            binding.tickCross.setImageDrawable(drawable)
            drawable.start()
            tick = !tick
        } else {

            showSnackBar(R.string.tick_cross_app_name)

        }
    }
}


