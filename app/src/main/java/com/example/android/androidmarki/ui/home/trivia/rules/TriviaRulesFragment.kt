package com.example.android.androidmarki.ui.home.trivia.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentTriviaGameBinding
import com.example.android.androidmarki.databinding.FragmentTriviaRulesBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.home.HomeListener

class TriviaRulesFragment : BaseFragment() {
    private lateinit var binding: FragmentTriviaRulesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentTriviaRulesBinding.inflate(inflater, container, false)
        return binding.root
    }
}
