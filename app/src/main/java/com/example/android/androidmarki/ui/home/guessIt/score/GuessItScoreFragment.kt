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

package com.example.android.androidmarki.ui.home.guessIt.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.androidmarki.databinding.FragmentGuessItScoreBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory

/**
 * Fragment where the final score is shown, after the game is over
 */
class GuessItScoreFragment : BaseFragment() {

    private lateinit var binding: FragmentGuessItScoreBinding
    override val isLandscape: Boolean
        get() = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        binding = FragmentGuessItScoreBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            val scoreFragmentArgs by navArgs<GuessItScoreFragmentArgs>()
            viewModel = this@GuessItScoreFragment.viewModels<GuessItScoreViewModel> {
                BaseViewModelFactory(GuessItScoreViewModel(scoreFragmentArgs.score))
            }.value
            // Specify the current activity as the lifecycle owner of the binding. This is used so that
            // the binding can observe LiveData updates
            lifecycleOwner = viewLifecycleOwner

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        navController = findNavController()
        // Navigates back to title when button is pressed
        binding.viewModel!!.eventPlayAgain.observe(viewLifecycleOwner, Observer { playAgain ->
            if (playAgain) {
                navController.navigate(GuessItScoreFragmentDirections.actionRestart())
                binding.viewModel!!.onPlayAgainComplete()
            }
        })
    }
}
