package com.example.android.androidmarki.ui.home.propertyAnimation

import android.animation.*
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentPropertyAnimationBinding
import com.example.android.androidmarki.ui.base.BaseFragment


/**
 * A simple [Fragment] subclass.
 * Use the [PropertyAnimationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PropertyAnimationFragment : BaseFragment() {
    private lateinit var binding: FragmentPropertyAnimationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPropertyAnimationBinding.inflate(inflater, container, false).apply {
            propertyAnimator = this@PropertyAnimationFragment
        }
        return binding.root
    }

    fun rotater() {

        // Rotate the view for a second around its center once

        val animator = ObjectAnimator.ofFloat(binding.star, View.ROTATION, -360f, 0f)
        animator.duration = 1000
        animator.disableViewDuringAnimation(binding.rotateButton)
        animator.start()
    }

    fun translater() {

        // Translate the view 200 pixels to the right and back

        val animator = ObjectAnimator.ofFloat(binding.star, View.TRANSLATION_X, 200f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(binding.translateButton)
        animator.start()
    }

    fun scaler() {

        // Scale the view up to 4x its default size and back

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(binding.star, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(binding.scaleButton)
        animator.start()
    }

    fun fader() {

        // Fade the view out to completely transparent and then back to completely opaque

        val animator = ObjectAnimator.ofFloat(binding.star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(binding.fadeButton)
        animator.start()
    }

    fun colorizer() {

        // Animate the color of the star's container from black to red over a half
        // second, then reverse back to black. Note that using a propertyName of
        // "backgroundColor" will cause the animator to call the backgroundColor property
        // (in Kotlin) or setBackgroundColor(int) (in Java).

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val animator = ObjectAnimator.ofArgb(
                binding.star.parent,
                "backgroundColor", Color.BLACK, Color.RED
            )
            animator.duration = 500
            animator.repeatCount = 1
            animator.repeatMode = ObjectAnimator.REVERSE
            animator.disableViewDuringAnimation(binding.colorizeButton)
            animator.start()
        }
    }

    fun shower() {

        // Create a new star view in a random X position above the container.
        // Make it rotateButton about its center as it falls to the bottom.

        // Local variables we'll need in the code below
        val container = binding.star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = binding.star.width.toFloat()
        var starH: Float = binding.star.height.toFloat()

        // Create the new star (an ImageView holding our drawable) and add it to the container
        val newStar = AppCompatImageView(context)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        container.addView(newStar)

        // Scale the view randomly between 10-160% of its default size
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY

        // Position the view at a random place between the left and right edges of the container
        newStar.translationX = Math.random().toFloat() * containerW - starW / 2

        // Create an animator that moves the view from a starting position right about the container
        // to an ending position right below the container. Set an accelerate interpolator to give
        // it a gravity/falling feel
        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH)
        mover.interpolator = AccelerateInterpolator(1f)

        // Create an animator to rotateButton the view around its center up to three times
        val rotator = ObjectAnimator.ofFloat(
            newStar, View.ROTATION,
            (Math.random() * 1080).toFloat()
        )
        rotator.interpolator = LinearInterpolator()

        // Use an AnimatorSet to play the falling and rotating animators in parallel for a duration
        // of a half-second to two seconds
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()

        // When the animation is done, remove the created view from the container
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })

        // Start the animation
        set.start()
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {

        // This extension method listens for start/end events on an animation and disables
        // the given view for the entirety of that animation.

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

}