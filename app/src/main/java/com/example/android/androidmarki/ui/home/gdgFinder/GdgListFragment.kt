package com.example.android.androidmarki.ui.home.gdgFinder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentGdgListBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory
import com.google.android.gms.location.*
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar


class GdgListFragment : BaseFragment() {
    private lateinit var binding: FragmentGdgListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGdgListBinding.inflate(inflater, container, false).apply {
            // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment

            lifecycleOwner = viewLifecycleOwner
            // Giving the binding access to the OverviewViewModel

            viewModel =
                ViewModelProvider(
                    this@GdgListFragment,
                    BaseViewModelFactory(GdgListViewModel())
                ).get(
                    GdgListViewModel::class.java
                )
            // Sets the adapter of the RecyclerView

            gdgChapterList.adapter = GdgListAdapter(GdgClickListener { chapter ->
                val destination = Uri.parse(chapter.website)
                startActivity(Intent(Intent.ACTION_VIEW, destination))
            })
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLastLocationOrStartLocationUpdates()

        binding.viewModel!!.showNeedLocation.observe(viewLifecycleOwner,
            Observer { show -> // Snackbar is like Toast but it lets us show forever
                if (show == true) {
                    Snackbar.make(
                        binding.root,
                        "No location. Enable location in settings (hint: test with Maps) then check app permissions!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })

        binding.viewModel!!.regionList.observe(viewLifecycleOwner, Observer {

            val chipGroup = binding.regionList
            val inflator = LayoutInflater.from(chipGroup.context)

            val children = it.map { regionName ->
                val chip = inflator.inflate(R.layout.gdg_region, chipGroup, false) as Chip
                chip.tag = regionName
                chip.text =   if (regionName.isNotEmpty())  regionName  else  "No Region"
                chip.setOnCheckedChangeListener { button, isChecked ->
                    binding.viewModel!!.onFilterChanged(button.tag as String, isChecked)
                }
                chip
            }

            chipGroup.removeAllViews()

            for (chip in children) {
                chipGroup.addView(chip)
            }

        })

    }



    /**
     * Show the user a dialog asking for permission to use location.
     */
    private fun requestLocationPermission() {
        /**
         * This will be called by Android when the user responds to the permission request.
         *
         * If granted, continue with the operation that the user gave us permission to do.
         */
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                requestLastLocationOrStartLocationUpdates()
            }
        }.launch(Manifest.permission.ACCESS_FINE_LOCATION)

    }

    /**
     * Request the last location of this device, if known, otherwise start location updates.
     *
     * The last location is cached from the last application to request location.
     */
    private fun requestLastLocationOrStartLocationUpdates() {
        // if we don't have permission ask for it and wait until the user grants it
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location == null) {
                startLocationUpdates(fusedLocationClient)
            } else {
                binding.viewModel!!.onLocationUpdated(location)
            }
        }
    }

    /**
     * Start location updates, this will ask the operating system to figure out the devices location.
     */
    private fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        // if we don't have permission ask for it and wait until the user grants it
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }


        val request = LocationRequest().setPriority(LocationRequest.PRIORITY_LOW_POWER)
        val callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                val location = locationResult?.lastLocation ?: return
                binding.viewModel!!.onLocationUpdated(location)
            }
        }
        fusedLocationClient.requestLocationUpdates(request, callback, null)
    }



}


