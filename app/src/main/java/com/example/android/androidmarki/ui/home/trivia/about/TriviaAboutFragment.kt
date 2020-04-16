package com.example.android.androidmarki.ui.home.trivia.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentTriviaAboutBinding
import com.example.android.androidmarki.databinding.FragmentTriviaGameBinding
import com.example.android.androidmarki.ui.base.BaseFragment

class TriviaAboutFragment : BaseFragment() {
    private lateinit var binding: FragmentTriviaAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTriviaAboutBinding.inflate(inflater, container, false).apply {
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

}}
