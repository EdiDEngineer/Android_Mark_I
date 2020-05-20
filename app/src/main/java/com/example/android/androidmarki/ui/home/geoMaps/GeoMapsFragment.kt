package com.example.android.androidmarki.ui.home.geoMaps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.android.androidmarki.R
import com.example.android.androidmarki.util.generateBitmapDescriptorFromRes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import timber.log.Timber
import java.util.*


class GeoMapsFragment : com.google.android.gms.maps.SupportMapFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val TAG = GeoMapsFragment::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        getMapAsync(this)
        setHasOptionsMenu(true)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near the Googleplex.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
        }

        //These coordinates represent the lattitude and longitude of the Googleplex.
        val latitude = 6.443523
        val longitude = 3.490639
//        The zoom level controls how zoomed in you are on the map. The following list gives you an idea of what level of detail each level of zoom shows:
//
//        1: World
//        5: Landmass/continent
//        10: City
//        15: Streets
//        20: Buildings
        val zoomLevel = 15f
        val overlaySize = 100f

        val homeLatLng = LatLng(latitude, longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        map.addMarker(MarkerOptions().position(homeLatLng))

        val googleOverlay = generateBitmapDescriptorFromRes(
        context,    R.drawable.splash_icon
        )?.let {
            GroundOverlayOptions()
                .image(
                    it
                )
                .position(homeLatLng, overlaySize)
        }
        map.addGroundOverlay(googleOverlay)

        setMapLongClick(map)
        setPoiClick(map)
        setMapStyle(map)
        enableMyLocation()
    }



    // Initializes contents of Activity's standard options menu. Only called the first time options
    // menu is displayed.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.geo_map_options, menu)
    }

    // Called whenever an item in your options menu is selected.
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Change the map type based on the user's selection.
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    // Called when user makes a long press gesture on the map.
    private fun setMapLongClick(map: GoogleMap) {

        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            map.addPolygon(
                PolygonOptions()
                    .add(
                        LatLng(
                            latLng.latitude,
                            latLng.longitude
                        ), LatLng(
                            latLng.latitude * 4,
                            latLng.longitude * 4
                        )
                    )
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE)
            )

        }

    }

    // Places a marker on the map and displays an info window that contains POI name.
    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker.showInfoWindow()
        }
    }

    // Allows map styling and theming to be customized.
    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style
                )
            )

            if (!success) {
                Timber.e("Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Timber.tag(TAG).e(e, "Can't find style. Error: ")
        }
    }

    // Checks that users have given permission
    private fun isPermissionGranted(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } == PackageManager.PERMISSION_GRANTED
    }


    // Checks if users have given their location and sets location enabled if so.
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    enableMyLocation()
                }
            }.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}