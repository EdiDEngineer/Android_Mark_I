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
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MultipleChaoticElementsFragment : BaseFragment() {
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

            val maxWidthOffset = 2f * resources.displayMetrics.widthPixels
            val maxHeightOffset = 2f * resources.displayMetrics.heightPixels
            val interpolator =
                AnimationUtils.loadInterpolator(
                    context,
                    android.R.interpolator.linear_out_slow_in
                )
            val random = Random()
            val count: Int = binding.roots.childCount
            for (i in 0 until count) {
                val view: View = binding.roots.getChildAt(i)
                view.visibility = View.VISIBLE
                view.alpha = 0.85f
                var xOffset = random.nextFloat() * maxWidthOffset
                if (random.nextBoolean()) {
                    xOffset *= -1f
                }
                view.translationX = xOffset
                var yOffset = random.nextFloat() * maxHeightOffset
                if (random.nextBoolean()) {
                    yOffset *= -1f
                }
                view.translationY = yOffset

                // now animate them back into their natural position
                view.animate()
                    .translationY(0f)
                    .translationX(0f)
                    .alpha(1f)
                    .setInterpolator(interpolator)
                    .setDuration(1000)
                    .start()
            }
        }else{
            showSnackBar(R.string.coordinated_motion_app_name)
        }
    }



}
