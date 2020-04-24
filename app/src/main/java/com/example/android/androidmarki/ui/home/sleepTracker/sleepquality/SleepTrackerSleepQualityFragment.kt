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

package com.example.android.androidmarki.ui.home.sleepTracker.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.data.local.AndroidMarkIDatabase
import com.example.android.androidmarki.databinding.FragmentSleepTrackerSleepQualityBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepTrackerSleepQualityFragment : BaseFragment() {
    private lateinit var binding: FragmentSleepTrackerSleepQualityBinding

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        binding =
            FragmentSleepTrackerSleepQualityBinding.inflate(inflater, container, false).apply {
                // Create an instance of the ViewModel Factory.
                lifecycleOwner = viewLifecycleOwner

                // Get a reference to the ViewModel associated with this fragment.
                viewModel =
                    ViewModelProvider(
                        this@SleepTrackerSleepQualityFragment, BaseViewModelFactory(
                            SleepTrackerSleepQualityViewModel(
                                SleepTrackerSleepQualityFragmentArgs.fromBundle(requireArguments()).sleepNightKey
                                ,
                                AndroidMarkIDatabase.getInstance(requireActivity().application).sleepTrackerDao
                            )
                        )
                    ).get(SleepTrackerSleepQualityViewModel::class.java).apply {
                        navController = findNavController()
//        // Add an Observer to the state variable for Navigating when a Quality icon is tapped.

                        navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
                            if (it == true) { // Observed state is true.
                                navController.navigate(
                                    SleepTrackerSleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment()
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
