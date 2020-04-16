package com.example.android.androidmarki.ui.home.trivia.gameOver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.databinding.FragmentTriviaGameOverBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.home.HomeListener

class TriviaGameOverFragment : BaseFragment() {

    private lateinit var binding: FragmentTriviaGameOverBinding
    val clickListener = object : HomeListener.TriviaGameOver {
        override fun onRetry() {
            navController.navigate(TriviaGameOverFragmentDirections.actionGameOverFragmentToGameFragment())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTriviaGameOverBinding.inflate(inflater, container, false).apply {
            gameOver = this@TriviaGameOverFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }
}
