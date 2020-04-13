package com.example.android.androidmarki.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.androidmarki.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Splash : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        launch {
            delay(3000)
            withContext(Dispatchers.Main) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }
    }
}
