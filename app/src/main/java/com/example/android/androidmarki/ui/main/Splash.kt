package com.example.android.androidmarki.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.base.BaseViewModel
import com.example.android.androidmarki.ui.home.HomeActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Splash : BaseActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    private val authenticationState = AuthenticateRepository.get().userLiveData.map { user ->
        when {
            user == null -> BaseViewModel.AuthenticationState.UNAUTHENTICATED
            user.phoneNumber.isNullOrEmpty() -> BaseViewModel.AuthenticationState.PHONE_UNVERIFIED
            !user.isEmailVerified -> BaseViewModel.AuthenticationState.EMAIL_UNVERIFIED
            else -> BaseViewModel.AuthenticationState.AUTHENTICATED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticationState.observe(
            this,
            Observer { authenticationState ->
                launch {
                    delay(3000)
                    if (authenticationState == BaseViewModel.AuthenticationState.AUTHENTICATED || authenticationState == BaseViewModel.AuthenticationState.EMAIL_UNVERIFIED) {
                        withContext(Dispatchers.Main) {
                            startActivity(Intent(applicationContext, HomeActivity::class.java))
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            startActivity(Intent(applicationContext, MainActivity::class.java))

                        }
                    }

                }
            })


    }
}
