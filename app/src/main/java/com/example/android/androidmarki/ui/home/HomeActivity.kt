package com.example.android.androidmarki.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.ActivityHomeBinding
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.base.BaseViewModelFactory
import com.example.android.androidmarki.ui.home.geoFence.GeoFenceFragment
import com.example.android.androidmarki.ui.home.home.HomeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val homeViewModel by viewModels<HomeViewModel> {
        BaseViewModelFactory(HomeViewModel())
//remember
    }
    private val navController by lazy {
        supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment)!!.findNavController()
    }

    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includeAppBarHome.toolbar)
        val fab: FloatingActionButton = binding.includeAppBarHome.fab
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            navController.graph, drawerLayout
        )

        navView.itemIconTintList = null


//        homeViewModel.authenticationState.observe(
//            this,
//            Observer { authenticationState ->
//                if (authenticationState == BaseViewModel.AuthenticationState.UNAUTHENTICATED) {
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                }
//
//            })


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            homeViewModel.userLiveData.signOut()
            navController.navigate(R.id.mainActivity)
            finish()
            true
        }

        navController.addOnDestinationChangedListener { _: NavController, nd: NavDestination, _: Bundle? ->

            if (nd.id == R.id.nav_home) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                fab.visibility = View.VISIBLE
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                fab.visibility = View.GONE
            }
            if (nd.id == R.id.nav_gdg_finder) {
                supportActionBar?.setDisplayShowTitleEnabled(false)
                binding.includeAppBarHome.heroImage.visibility = View.VISIBLE
            } else {
                supportActionBar?.setDisplayShowTitleEnabled(true)
                binding.includeAppBarHome.heroImage.visibility = View.GONE
            }
        }
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        // Hide the keyboard.
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /*
 *  When the user clicks on the notification, this method will be called, letting us know that
 *  the geofence has been triggered, and it's time to move to the next one in the treasure
 *  hunt.
 */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (fragment is GeoFenceFragment) {
            fragment.onNewIntent(
                intent?.extras
            )
        }
    }

}
