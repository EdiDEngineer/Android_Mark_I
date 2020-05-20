/*
 * Copyright (C) 2019 Google Inc.
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

package com.example.android.androidmarki.ui.home.geoFence

import android.Manifest
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.library.BuildConfig
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentGeoFenceBinding
import com.example.android.androidmarki.receiver.GeoFenceBroadcastReceiver
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.util.GeofencingConstants
import com.example.android.androidmarki.util.createChannel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

/**
 * The Treasure Hunt app is a single-player game based on geofences.
 *
 * This app demonstrates how to create and remove geofences using the GeofencingApi. Uses an
 * BroadcastReceiver to monitor geofence transitions and creates notification and finishes the game
 * when the user enters the final geofence (destination).
 *
 * This app requires a device's Location settings to be turned on. It also requires
 * the ACCESS_FINE_LOCATION permission and user consent. For geofences to work
 * in Android Q, app also needs the ACCESS_BACKGROUND_LOCATION permission and user consent.
 */

class GeoFenceFragment : BaseFragment() {

    private lateinit var binding: FragmentGeoFenceBinding
    private lateinit var geofencingClient: GeofencingClient
    private val TAG = GeoFenceFragment::class.java.simpleName

    private val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    // A PendingIntent for the Broadcast Receiver that handles geofence transitions.
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeoFenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        // Use FLAG_UPDATE_CURRENT so that you get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeoFenceBinding.inflate(inflater, container, false).apply {
            viewmodel =this@GeoFenceFragment.viewModels<GeoFenceViewModel> {    SavedStateViewModelFactory(requireActivity().application, this@GeoFenceFragment) }.value
            lifecycleOwner = viewLifecycleOwner

        }


        geofencingClient = context?.let { LocationServices.getGeofencingClient(it) }!!

        // Create channel for notifications
        createChannel(context)
        layoutView = binding.root
        return layoutView
    }
    private lateinit var applicationContext: Context



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
   checkPermissionsAndStartGeofencing()
        applicationContext =requireContext().applicationContext

    }



    /*
     *  When the user clicks on the notification, this method will be called, letting us know that
     *  the geofence has been triggered, and it's time to move to the next one in the treasure
     *  hunt.
     */
    fun onNewIntent(extras: Bundle?) {
        if (extras != null) {
            if (extras.containsKey(GeofencingConstants.EXTRA_GEOFENCE_INDEX)) {
                binding.viewmodel!!.updateHint(extras.getInt(GeofencingConstants.EXTRA_GEOFENCE_INDEX))
                checkPermissionsAndStartGeofencing()
            }
        }
    }


    override fun onDetach() {
        super.onDetach()
    removeGeofences()
    }

    /**
     * Starts the permission check and Geofence process only if the Geofence associated with the
     * current hint isn't yet active.
     */
    private fun checkPermissionsAndStartGeofencing() {
        if (binding.viewmodel!!.geofenceIsActive()) return
        if (foregroundAndBackgroundLocationPermissionApproved()) {
            checkDeviceLocationSettingsAndStartGeofence()
        } else {
            requestForegroundAndBackgroundLocationPermissions()
        }
    }

    /*
     *  Uses the Location Client to check the current state of location settings, and gives the user
     *  the opportunity to turn on location services within our app.
     */
    private fun checkDeviceLocationSettingsAndStartGeofence(resolve: Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = context?.let { LocationServices.getSettingsClient(it) }
        val locationSettingsResponseTask =
            settingsClient?.checkLocationSettings(builder.build())

        locationSettingsResponseTask?.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {


                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().

                    registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                        /*
        *  When we get the result from asking the user to turn on device location, we call
        *  checkDeviceLocationSettingsAndStartGeofence again to make sure it's actually on, but
        *  we don't resolve the check to keep the user from seeing an endless loop.
        */
                        // We don't rely on the result code, but just check the location setting again
                        checkDeviceLocationSettingsAndStartGeofence(false)
                    }.launch(
                        IntentSenderRequest.Builder(exception.resolution).build()
                    )

                } catch (sendEx: IntentSender.SendIntentException) {
                    Timber.tag(TAG)
                        .d("Error geting location settings resolution: %s", sendEx.message)
                }
            } else {
                Snackbar.make(
                    binding.activityMapsMain,
                    R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndStartGeofence()
                }.show()
            }
        }
        locationSettingsResponseTask?.addOnCompleteListener {
            if (it.isSuccessful) {
                addGeofenceForClue()
            }
        }
    }

    /*
     *  Determines whether the app has the appropriate permissions across Android 10+ and all other
     *  Android versions.
     */
    @TargetApi(29)
    private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        context?.let {
                            ActivityCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        })
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                PackageManager.PERMISSION_GRANTED ==
                        context?.let {
                            ActivityCompat.checkSelfPermission(
                                it, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                        }
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    /*
     *  Requests ACCESS_FINE_LOCATION and (on Android 10+ (Q) ACCESS_BACKGROUND_LOCATION.
     */
    @TargetApi(29)
    private fun requestForegroundAndBackgroundLocationPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved())
            return

        // Else request the permission
        // this provides the result[LOCATION_PERMISSION_INDEX]
        val permissionsArray =
            when {
                runningQOrLater -> {
                    // this provides the result[BACKGROUND_LOCATION_PERMISSION_INDEX]
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
                else ->  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            }



        Timber.tag(TAG).d("Request foreground only location permission")
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            /*
* In all cases, we need to have the location permission.  On Android 10+ (Q) we need to have
* the background permission as well.
*/
            Timber.tag(TAG).d("onRequestPermissionResult")

            if((permissionsArray.size==1&& it[permissionsArray[0]] == false)||(permissionsArray.size == 2 &&it[permissionsArray[0]] == false &&it[permissionsArray[1]]== false)){
                // Permission denied.
                Snackbar.make(
                    binding.activityMapsMain,
                    R.string.permission_denied_explanation, Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.settings) {
                        // Displays App settings screen.
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.LIBRARY_PACKAGE_NAME, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }.show()
            } else {
                checkDeviceLocationSettingsAndStartGeofence()
            }
        }.launch(permissionsArray)


    }

    /*
     * Adds a Geofence for the current clue if needed, and removes any existing Geofence. This
     * method should be called after the user has granted the location permission.  If there are
     * no more geofences, we remove the geofence and let the viewmodel know that the ending hint
     * is now "active."
     */
    private fun addGeofenceForClue() {
        if (binding.viewmodel!!.geofenceIsActive()) return
        val currentGeofenceIndex = binding.viewmodel!!.nextGeofenceIndex()
        if (currentGeofenceIndex >= GeofencingConstants.NUM_LANDMARKS) {
            removeGeofences()
            binding.viewmodel!!.geofenceActivated()
            return
        }
        val currentGeofenceData = GeofencingConstants.LANDMARK_DATA[currentGeofenceIndex]

        // Build the Geofence Object
        val geofence = Geofence.Builder()
            // Set the request ID, string to identify the geofence.
            .setRequestId(currentGeofenceData.id)
            // Set the circular region of this geofence.
            .setCircularRegion(
                currentGeofenceData.latLong.latitude,
                currentGeofenceData.latLong.longitude,
                GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
            )
            // Set the expiration duration of the geofence. This geofence gets
            // automatically removed after this period of time.
            .setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
            // Set the transition types of interest. Alerts are only generated for these
            // transition. We track entry and exit transitions in this sample.
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        // Build the geofence request
        val geofencingRequest = GeofencingRequest.Builder()
            // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
            // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
            // is already inside that geofence.
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)

            // Add the geofences to be monitored by geofencing service.
            .addGeofence(geofence)
            .build()

        // First, remove any existing geofences that use our pending intent
        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            // Regardless of success/failure of the removal, add the new geofence
            addOnCompleteListener {
                // Add the new geofence request with the new geofence
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
                    addOnSuccessListener {
                        // Geofences added.
                        showShortToast(R.string.geofences_added)

                        Timber.tag("Add Geofence").e(geofence.requestId)
                        // Tell the viewmodel that we've reached the end of the game and
                        // activated the last "geofence" --- by removing the Geofence.
                        binding.viewmodel!!.geofenceActivated()
                    }
                    addOnFailureListener {
                        // Failed to add geofences.
                        showShortToast(R.string.geofences_not_added)
                        if ((it.message != null)) {
                            Timber.tag(TAG).w(it)
                        }
                    }
                }
            }
        }
    }


    /**
     * Removes geofences. This method should be called after the user has granted the location
     * permission.
     */
    private fun removeGeofences() {
        if (!foregroundAndBackgroundLocationPermissionApproved()) {
            return
        }

        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences removed
                Timber.d(applicationContext.getString(R.string.geofences_removed))
                Toast.makeText(applicationContext,R.string.geofences_removed, Toast.LENGTH_SHORT).show()
            }
            addOnFailureListener {
                // Failed to remove geofences
                Timber.d(applicationContext.getString(R.string.geofences_not_removed))
            }
        }
    }

    companion object {
        internal const val ACTION_GEOFENCE_EVENT =
            "TreasureHunt.action.ACTION_GEOFENCE_EVENT"
    }



}


