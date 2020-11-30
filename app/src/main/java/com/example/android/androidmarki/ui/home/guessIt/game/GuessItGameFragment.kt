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

package com.example.android.androidmarki.ui.home.guessIt.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.databinding.FragmentGuessItGameBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory


/**
 * Fragment where the game is played
 */
class GuessItGameFragment : BaseFragment() {


    private lateinit var binding: FragmentGuessItGameBinding
    override val isLandscape: Boolean
        get() = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = FragmentGuessItGameBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            // Get the viewmodel

            viewModel = ViewModelProvider(
                this@GuessItGameFragment,
                BaseViewModelFactory(GuessItGameViewModel())
            ).get(GuessItGameViewModel::class.java)

            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        // Sets up event listening to navigate the player when the game is finished
        binding.viewModel!!.eventGameFinish.observe(viewLifecycleOwner, Observer { isFinished ->
            if (isFinished) {
                val currentScore = binding.viewModel!!.score.value ?: 0
                val action = GuessItGameFragmentDirections.actionGameToScore(currentScore)
                navController.navigate(action)
                binding.viewModel!!.onGameFinishComplete()
            }
        })

        // Buzzes when triggered with different buzz events
        binding.viewModel!!.eventBuzz.observe(viewLifecycleOwner, Observer { buzzType ->
            if (buzzType != GuessItGameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                binding.viewModel!!.onBuzzComplete()
            }
        })

    }

    /**
     * Given a pattern, this method makes sure the device buzzes
     */
    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}
