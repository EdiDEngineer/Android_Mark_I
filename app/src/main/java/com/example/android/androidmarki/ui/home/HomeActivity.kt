package com.example.android.androidmarki.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.ActivityHomeBinding
import com.example.android.androidmarki.databinding.AppBarHomeBinding
import com.example.android.androidmarki.databinding.ContentHomeBinding
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
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)//issues with nav host fragment couldnt
        setContentView(binding.root)

        setSupportActionBar(binding.includeAppBarHome.toolbar)
        val fab: FloatingActionButton = binding.includeAppBarHome.fab
       fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        drawerLayout =binding.drawerLayout
        val navView: NavigationView = binding.navView


        val navController = home_nav_host_fragment.findNavController()


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_trivia, R.id.nav_logout
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
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.home_nav_host_fragment)
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
