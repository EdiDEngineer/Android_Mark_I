package com.example.android.androidmarki.ui.home.coordinatedMotion

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentMultipleElementsBinding
import com.example.android.androidmarki.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class MultipleElementsFragment : BaseFragment() {
private lateinit var binding: FragmentMultipleElementsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMultipleElementsBinding.inflate(inflater, container, false)
        layoutView= binding.root
        return layoutView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animateViewsIn()
        binding.roots.setOnClickListener {
            animateViewsIn()
        }
    }
    private fun animateViewsIn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        val count = binding.roots.childCount
        var offset = resources.getDimensionPixelSize(R.dimen.offset_y).toFloat()
        val interpolator =
            AnimationUtils.loadInterpolator(
                context,
                android.R.interpolator.linear_out_slow_in            )

        // loop over the children setting an increasing translation y but the same animation
        // duration + interpolation
        for (i in 0 until count) {
            val view = binding.roots.getChildAt(i)
            view.visibility = View.VISIBLE
            view.translationY = offset
            view.alpha = 0.85f
            // then animate back to natural position
            view.animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(interpolator)
                .setDuration(1000L)
                .start()
            // increase the offset distance for the next view
            offset *= 1.5f
        }}else{
            showSnackBar(R.string.coordinated_motion_app_name)
        }
    }



}
