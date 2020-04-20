/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.androidmarki.ui.home.guessIt.title

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.databinding.FragmentGuessItTitleBinding
import com.example.android.androidmarki.ui.base.BaseFragment


/**
 * Fragment for the starting or title screen of the app
 */
class GuessItTitleFragment : BaseFragment() {

    private lateinit var binding: FragmentGuessItTitleBinding
    override val isLandscape: Boolean
        get() = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGuessItTitleBinding.inflate(
            inflater, container, false
        ).apply {
            title = this@GuessItTitleFragment
        }

        requireActivity().requestedOrientation
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    fun onPlay() {
        navController.navigate(GuessItTitleFragmentDirections.actionTitleToGame())
    }


}
