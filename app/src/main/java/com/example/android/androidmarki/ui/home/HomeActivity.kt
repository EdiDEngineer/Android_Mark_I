package com.example.android.androidmarki.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.ActivityHomeBinding
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.base.BaseViewModelFactory
import com.example.android.androidmarki.ui.home.home.HomeViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_home.*


class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val homeViewModel by viewModels<HomeViewModel> {
        BaseViewModelFactory(HomeViewModel())
//remember
    }
    private val navController by lazy {
        home_nav_host_fragment.findNavController()
    }

lateinit var drawerLayout: DrawerLayout
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
            setOf(
                R.id.nav_home,
                R.id.nav_dessert_pusher,
                R.id.nav_dice_roller,
                R.id.triviaTitleFragment,
                R.id.guessItTitleDestination,
                R.id.sleep_tracker_fragment,
                R.id.nav_logout
            ), drawerLayout
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
        val bottomNavView: BottomNavigationView = binding.bottomNavView

        val badge: BadgeDrawable = bottomNavView.getOrCreateBadge(R.id.navigation_dashboard)
        badge.isVisible = true

        bottomNavView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            homeViewModel.userLiveData.signOut()
            navController.navigate(R.id.mainActivity)
            finish()
            true
        }
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            if (nd.id == R.id.nav_home || nd.id == R.id.nav_dessert_pusher || nd.id == R.id.sleep_tracker_fragment || nd.id == R.id.nav_dice_roller || nd.id == R.id.guessItTitleDestination|| nd.id == R.id.triviaTitleFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
            if (nd.id == R.id.nav_home || nd.id == R.id.navigation_dashboard || nd.id == R.id.navigation_notifications) {
                bottomNavView.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            } else {
                bottomNavView.visibility = View.GONE
                fab.visibility = View.GONE
            }
        }

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
}
