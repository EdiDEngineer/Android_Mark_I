package com.example.android.androidmarki.ui.firebase

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.ui.base.BaseActivity
import com.google.android.material.badge.BadgeDrawable

class FirebaseActivity : BaseActivity() {
private lateinit var  navController :NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase)


        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        navController =         supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!.findNavController()
//        setSupportActionBar(findViewById(R.id. firebase_toolbar))
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
   appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        val bottomNavView: BottomNavigationView = binding.bottomNavView

//        val badge: BadgeDrawable = bottomNavView.getOrCreateBadge(R.id.navigation_dashboard)
//        badge.isVisible = true

//        bottomNavView.setupWithNavController(navController)
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
