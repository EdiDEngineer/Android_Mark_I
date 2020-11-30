/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.androidmarki.ui.home.sleepTracker.sleepdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.androidmarki.data.local.AndroidMarkIDatabase
import com.example.android.androidmarki.databinding.FragmentSleepTrackerSleepDetailBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SleepTrackerSleepDetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SleepTrackerSleepDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SleepTrackerSleepDetailFragment : BaseFragment() {
    private lateinit var binding: FragmentSleepTrackerSleepDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Get a reference to the binding object and inflate the fragment views.
        binding = FragmentSleepTrackerSleepDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
// To use the View Model with data binding, you have to explicitly

            // Create an instance of the ViewModel Factory.
            viewModel = ViewModelProvider(
                this@SleepTrackerSleepDetailFragment,
                BaseViewModelFactory(
                    SleepTrackerSleepDetailViewModel(
                        navArgs<SleepTrackerSleepDetailFragmentArgs>().value.sleepNightKey,
                        AndroidMarkIDatabase.getDatabaseInstance(requireActivity().application).sleepTrackerDao
                    )
                )
            ).get(
                SleepTrackerSleepDetailViewModel::class.java
            ).apply {
                // give the binding object a reference to it.
                navController = findNavController()

                // Add an Observer to the state variable for Navigating when a Quality icon is tapped.
                navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
                    if (it == true) { // Observed state is true.
                        navController.navigate(
                            SleepTrackerSleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment()
                        )
                        // Reset state to make sure we only navigate once, even if the device
                        // has a configuration change.
                        doneNavigating()
                    }
                })
            }

        }


        return binding.root

    }

}
