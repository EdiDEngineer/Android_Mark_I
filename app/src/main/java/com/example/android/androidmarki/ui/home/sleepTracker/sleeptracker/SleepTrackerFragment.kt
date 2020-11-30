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

package com.example.android.androidmarki.ui.home.sleepTracker.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.local.AndroidMarkIDatabase
import com.example.android.androidmarki.databinding.FragmentSleepTrackerBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : BaseFragment() {
    private lateinit var binding: FragmentSleepTrackerBinding

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
        binding = FragmentSleepTrackerBinding.inflate(inflater, container, false).apply {
            navController = findNavController()
            lifecycleOwner = viewLifecycleOwner
            viewModel =
                ViewModelProvider(
                    this@SleepTrackerFragment, BaseViewModelFactory(
                        SleepTrackerViewModel(AndroidMarkIDatabase.getDatabaseInstance(requireActivity().application).sleepTrackerDao)
                    )
                ).get(SleepTrackerViewModel::class.java).apply {
                    // Add an Observer on the state variable for showing a Snackbar message
                    // when the CLEAR button is pressed.
                    showSnackBarEvent.observe(viewLifecycleOwner, Observer {
                        if (it == true) { // Observed state is true.

                            showSnackBar(R.string.cleared_message)
                            // Reset state to make sure the snackbar is only shown once, even if the device
                            // has a configuration change.
                            doneShowingSnackbar()
                        }
                    })

                    // Add an Observer on the state variable for Navigating when STOP button is pressed.
                    navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
                        night?.let {
                            // We need to get the navController from this, because button is not ready, and it
                            // just has to be a view. For some reason, this only matters if we hit stop again
                            // after using the back button, not if we hit stop and choose a quality.
                            // Also, in the Navigation Editor, for Quality -> Tracker, check "Inclusive" for
                            // popping the stack to get the correct behavior if we press stop multiple times
                            // followed by back.
                            // Also: https://stackoverflow.com/questions/28929637/difference-and-uses-of-oncreate-oncreateview-and-onactivitycreated-in-fra
                            navController.navigate(
                                SleepTrackerFragmentDirections
                                    .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId)
                            )
                            // Reset state to make sure we only navigate once, even if the device
                            // has a configuration change.
                            doneNavigating()
                        }
                    })

                    navigateToSleepDataQuality.observe(
                        viewLifecycleOwner,
                        Observer { night ->
                            night?.let {

                                navController.navigate(
                                    SleepTrackerFragmentDirections
                                        .actionSleepTrackerFragmentToSleepDetailFragment(night)
                                )
                                onSleepDataQualityNavigated()
                            }
                        })
                    val manager = GridLayoutManager(activity, 3)
                    sleepList.layoutManager = manager

                    manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int) = when (position) {
                            0 -> 3
                            else -> 1
                        }
                    }

                    val adapter = SleepNightAdapter(SleepNightListener { nightId ->
                        onSleepNightClicked(nightId)
                    })

                    sleepList.adapter = adapter

                    nights.observe(viewLifecycleOwner, Observer {
                        it?.let {
                            adapter.addHeaderAndSubmitList(it)
                        }
                    })

                }

        }

        layoutView = binding.root
        return layoutView
    }
}
