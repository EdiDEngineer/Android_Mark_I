package com.example.android.androidmarki.ui.home.trivia.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentTriviaGameBinding
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory

class TriviaGameFragment : BaseFragment() {
    private lateinit var binding: FragmentTriviaGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTriviaGameBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProvider(
                this@TriviaGameFragment,
                BaseViewModelFactory(TriviaGameViewModel())
            ).get(
                TriviaGameViewModel::class.java
            )
            lifecycleOwner = viewLifecycleOwner

        }

        layoutView = binding.root
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.viewModel!!.gameUIData.observe(viewLifecycleOwner, Observer {
            if (it.isWon) {
                navController.navigate(
                    TriviaGameFragmentDirections.actionGameFragmentToGameWonFragment(
                        binding.viewModel!!.questionIndex.value!!,
                        binding.viewModel!!.numQuestions
                    )
                )
                binding.viewModel!!.onClear()
            }

            if (it.isLost) {
                navController.navigate(TriviaGameFragmentDirections.actionGameFragmentToGameOverFragment())
                binding.viewModel!!.onClear()
            }
            if (it.error != null) {
                showSnackBar(it.error!!)
                binding.viewModel!!.onClear() }
        })
        binding.viewModel!!.questionIndex.observe(viewLifecycleOwner, Observer {
            (activity as BaseActivity).supportActionBar?.title =
                getString(
                    R.string.title_android_trivia_question,
                    it + 1,
                    binding.viewModel!!.numQuestions
                )
        })

    }
}
