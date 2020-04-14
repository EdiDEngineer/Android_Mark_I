package com.example.android.androidmarki.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.ActivitySplashBinding
import com.example.android.androidmarki.ui.base.BaseActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Splash : BaseActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        launch {
            delay(3000)
            withContext(Dispatchers.Main) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }
    }
}
