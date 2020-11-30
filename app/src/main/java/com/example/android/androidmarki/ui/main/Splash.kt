package com.example.android.androidmarki.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.data.repository.AuthenticationState
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.home.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : BaseActivity() {
    private val authenticationState = AuthenticateRepository().authenticationState
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticationState.observe(
            this,
            Observer { authenticationState ->
                lifecycleScope.launch {
                    delay(1000)
                    if (authenticationState == AuthenticationState.AUTHENTICATED || authenticationState == AuthenticationState.EMAIL_UNVERIFIED) {

                        // Check if we're running on Android 5.0 or higher
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Apply activity transition

//                            startActivity(
//                                Intent(this@Splash, HomeActivity::class.java)
//                                ,
//                                ActivityOptions.makeSceneTransitionAnimation(this@Splash).toBundle()
//                            )
//                            finishAfterTransition()
//                        } else {
                        startActivity(
                            Intent(this@Splash, HomeActivity::class.java)
                        )
//                        }
                    } else {
                        // Check if we're running on Android 5.0 or higher
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Apply activity transition
//                            startActivity(
//                                Intent(this@Splash, MainActivity::class.java)
//                                ,
//                                ActivityOptions.makeSceneTransitionAnimation(this@Splash).toBundle()
//                            )
//                            finishAfterTransition()
//                        } else {
                        startActivity(
                            Intent(this@Splash, MainActivity::class.java)
                        )
//                        }
                    }
                }
            })

    }

}
