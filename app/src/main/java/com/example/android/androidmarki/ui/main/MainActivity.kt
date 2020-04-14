package com.example.android.androidmarki.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.ActivityMainBinding
import com.example.android.androidmarki.ui.base.BaseActivity

class MainActivity : BaseActivity() {


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
