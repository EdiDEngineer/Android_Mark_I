package com.example.android.androidmarki.ui.base

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.android.androidmarki.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var  locationString: String
    private lateinit var dialog : Dialog
    private val builder: MaterialAlertDialogBuilder by lazy {
        MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_App_MaterialAlertDialog
        )
    }
    protected lateinit var snackView: View

    var frameId = -1

    var TAG = "-1"

//    private lateinit var locationUtil: LocationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        locationUtil = LocationUtil(this, this)

        //initCustomFonts()

        //Fabric.with(this, Crashlytics())
//
//        checkLocationPermission()
//        changeLocationSettings()
//        getLocation()
    }

    fun getLocationUpdates() = locationString

//    fun getLatLonUpdates() = locationUtil.latLon

//    override fun attachBaseContext(newBase: Context) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
//    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ActivityCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(
            this,
            permissions,
            REQUEST_LOCATION_PERMISSION
        )
    }


    private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
        beginTransaction().apply {
            action()
        }.commit()
    }

    fun addFragmentToActivity(fragment: Fragment?, container: Int, tag: String) {
        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(container, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }

    private fun initCustomFonts() {
//        ViewPump.init(
//            ViewPump.builder()
//                .addInterceptor(
//                    CalligraphyInterceptor(
//                        CalligraphyConfig.Builder()
//                            .setDefaultFontPath("fonts/PT_Sans-Web-Regular.ttf")
//                            .setFontAttrId(R.attr.fontPath)
//                            .build()
//                    )
//                )
//                .build()
//        )
    }

    fun showDialog(
        title: String,
        message: String? = null,
        positiveText: String = "OK",
        negativeText: String = "Cancel",
        neutralText: String = "",
        negativeAction: (dialog: DialogInterface, id: Int) -> Unit = { dialog, _ -> dialog.dismiss() },
        neutralAction: ((dialog: DialogInterface, id: Int) -> Unit)? = null,
        positiveAction: (dialog: DialogInterface, id: Int) -> Unit
    ) {
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText) { dialog, id ->
                positiveAction(dialog, id)
            }
            .setNegativeButton(negativeText) { dialog, id ->
                negativeAction(dialog, id)
            }

        if (neutralAction != null) {
            builder.setNeutralButton(neutralText) { dialog, which ->
                neutralAction(dialog, which)
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun checkLocationPermission() {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            !hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ||
            !hasPermission(Manifest.permission.CAMERA)
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
//                showDialog(
//                    getString(R.string.dialog_title_location_permission),
//                    getString(R.string.location_permission_rationale)
//                ) { _, _ ->
//                    requestLocationPermission()
//                }
            } else {
                requestLocationPermission()
            }
        }
        getLocation()
    }

    private fun requestLocationPermission() {
        requestLocationPermission(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    private fun getLocation(){
//        locationUtil.getLocationLiveData().observe(this, Observer {location ->
//            locationString = location ?: getString(R.string.no_location)
//        })
    }

    private fun showLongToast( message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showShortToast( message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun changeLocationSettings() {
//        locationUtil.changeLocationSettings()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                ) {
//                    showShortToast(this, getString(R.string.location_perm_granted))
                } else {
//                    showLongToast(this, getString(R.string.location_perm_denied))
                    requestLocationPermission()
                }
                return
            }
        }
    }

    fun showSnackBar(@StringRes error: Int) {
        Snackbar.make(snackView, error, Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackBar(error: String, actionTitle: String, listener: View.OnClickListener) {
        Snackbar.make(snackView, error, Snackbar.LENGTH_LONG)
            .setAction(actionTitle, listener)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) getLocation()
//        else changeLocationSettings()
    }

    fun showLoadingDialog(){
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
//        dialog.setContentView(R.layout.preloader_layout)
        dialog.show()

//        val dialogBuilder =  AlertDialog.Builder(this);
//        //val view = LayoutInflater.from(this).inflate(R.layout.preloader_layout, null)
//        dialogBuilder.setView(R.layout.preloader_layout);
//        dialogBuilder.setCancelable(true);
//        dialogBuilder.create().show();
    }

    fun hideLoadingDialog(){
        dialog.hide()
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 129
    }
}