package com.example.android.androidmarki.ui.home.coordinatedMotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.databinding.FragmentCoordinatedMotionBinding
import com.example.android.androidmarki.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class CoordinatedMotionFragment : BaseFragment() {
    private lateinit var binding: FragmentCoordinatedMotionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoordinatedMotionBinding.inflate(inflater, container, false).apply {
            coordinateMotion = this@CoordinatedMotionFragment
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= findNavController()

    }
    fun multipleElementsClick() {
        navController.navigate(CoordinatedMotionFragmentDirections.actionCoordinatedMotionToMultipleElementsFragment())
    }

    fun multipleChaoticElementsClick() {
        navController.navigate(CoordinatedMotionFragmentDirections.actionCoordinatedMotionToMultipleChaoticElementsFragment())
    }

    fun curvedMotionClick() {
        navController.navigate(CoordinatedMotionFragmentDirections.actionCoordinatedMotionToCurveMotionListFragment())
    }

    fun sizeChangeClick() {
        navController.navigate(CoordinatedMotionFragmentDirections.actionCoordinatedMotionToCoordinateMotionSizeChangeFragment())
    }
}
