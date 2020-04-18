package com.example.android.androidmarki.ui.main

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.databinding.ActivityMainBinding
import com.example.android.androidmarki.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
