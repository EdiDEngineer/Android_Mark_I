package com.example.android.androidmarki.ui.home.heartFill

import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat.postOnAnimation
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentHeartFillBinding
import com.example.android.androidmarki.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class HeartFillFragment : BaseFragment() {
    private lateinit var binding: FragmentHeartFillBinding
    private var full = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHeartFillBinding.inflate(inflater, container, false).apply {
            heartFill = this@HeartFillFragment
        }
        return binding.root
    }

    fun animate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val drawable =
                if (full) context?.getDrawable(R.drawable.avd_heart_empty) else context?.getDrawable(
                    R.drawable.avd_heart_fill
                )
            binding.imageView.setImageDrawable(drawable)
            (drawable as Animatable).start()


            full = !full
        } else {
            showSnackBar(R.string.heart_fill_app_name)
        }
    }

}
