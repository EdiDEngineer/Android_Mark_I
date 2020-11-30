package com.example.android.androidmarki.ui.home.coordinatedMotion

import android.content.Intent.getIntent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentCurvedMotionDetailBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_curved_motion_detail.*

/**
 * A simple [Fragment] subclass.
 */
class CurvedMotionDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentCurvedMotionDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentCurvedMotionDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // tint the circle to match the launching activity
        // tint the circle to match the launching activity

        avatar.backgroundTintList = ColorStateList.valueOf(
            navArgs<CurvedMotionDetailFragmentArgs>().value.EXTRACOLOR
        )

        // check if we should used curved motion and load an appropriate transition

        // check if we should used curved motion and load an appropriate transition
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(if (   CurvedMotionDetailFragmentArgs.fromBundle(requireArguments()).EXTRACURVE) R.transition.explode else R.transition.curve)

        // Add these two lines below
    }

}
