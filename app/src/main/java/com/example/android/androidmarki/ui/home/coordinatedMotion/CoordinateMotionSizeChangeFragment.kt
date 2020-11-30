package com.example.android.androidmarki.ui.home.coordinatedMotion

import android.R
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.android.androidmarki.databinding.FragmentCoordinateMotionSizeChangeBinding
import com.example.android.androidmarki.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class CoordinateMotionSizeChangeFragment : BaseFragment() {
private lateinit var binding: FragmentCoordinateMotionSizeChangeBinding

    val LARGE_SCALE = 1.5f
    private var symmetric = true
    private var small = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCoordinateMotionSizeChangeBinding.inflate(inflater,container, false).apply {
            sizeChange = this@CoordinateMotionSizeChangeFragment
        }
        layoutView = binding.root
        return layoutView
    }
    fun changeSize() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val interpolator =
            AnimationUtils.loadInterpolator(
                context,
                R.interpolator.fast_out_slow_in
            )
        val scaleX = ObjectAnimator.ofFloat(
           binding. card,
            View.SCALE_X,
            if (small) LARGE_SCALE else 1f
        )
        scaleX.interpolator = interpolator
        scaleX.duration = if (symmetric) 600L else 200L
        val scaleY = ObjectAnimator.ofFloat(
            binding.     card,
            View.SCALE_Y,
            if (small) LARGE_SCALE else 1f
        )
        scaleY.interpolator = interpolator
        scaleY.duration = 600L
        scaleX.start()
        scaleY.start()

        // toggle the state so that we switch between large/small and symmetric/asymmetric
        small = !small
        if (small) {
            symmetric = !symmetric
        }}else{
            showSnackBar(com.example.android.androidmarki.R.string.coordinated_motion_app_name)
        }
    }

}
