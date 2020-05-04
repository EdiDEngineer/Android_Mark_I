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
 *
 */

package com.example.android.androidmarki.ui.home.marsRealEstate.overview

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.remote.network.service.MarsApiFilter
import com.example.android.androidmarki.databinding.FragmentMarsRealEstateOverviewBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class MarsRealEstateOverviewFragment : BaseFragment() {


    private lateinit var binding: FragmentMarsRealEstateOverviewBinding

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarsRealEstateOverviewBinding.inflate(inflater, container, false).apply {
           // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment

            lifecycleOwner = viewLifecycleOwner


            // Giving the binding access to the OverviewViewModel

            viewModel = ViewModelProvider(
                this@MarsRealEstateOverviewFragment,
                BaseViewModelFactory(OverviewViewModel())
            ).get(OverviewViewModel::class.java)
            // Sets the adapter of the photosGrid RecyclerView with clickHandler lambda that
            // tells the viewModel when our property is clicked
            photosGrid.adapter =
                MarsRealEstatePhotoGridAdapter(MarsRealEstatePhotoGridAdapter.OnClickListener {
                    viewModel!!.displayPropertyDetails(it)
                })
        }



        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        binding.viewModel!!.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                // Must find the NavController from the Fragment
                navController.navigate(MarsRealEstateOverviewFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                binding.viewModel!!.displayPropertyDetailsComplete()
            }
        })

    }
    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mars_real_estate_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Updates the filter in the [OverviewViewModel] when the menu items are selected from the
     * overflow menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        binding.viewModel!!.updateFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
                R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
                else -> MarsApiFilter.SHOW_ALL
            }

        )
        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        )
                || super.onOptionsItemSelected(item)
    }
}
