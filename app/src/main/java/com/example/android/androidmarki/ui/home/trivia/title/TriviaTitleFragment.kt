package com.example.android.androidmarki.ui.home.trivia.title

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentTriviaTitleBinding
import com.example.android.androidmarki.ui.base.BaseFragment

class TriviaTitleFragment : BaseFragment() {

    private lateinit var binding: FragmentTriviaTitleBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTriviaTitleBinding.inflate(inflater, container, false).apply {
            title = this@TriviaTitleFragment
        }
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.trivia_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.triviaAboutFragment -> {
                navController.navigate(TriviaTitleFragmentDirections.actionTriviaTitleFragmentToTriviaAboutFragment())
                true
            }
            R.id.triviaRulesFragment -> {
                navController.navigate(TriviaTitleFragmentDirections.actionTriviaTitleFragmentToTriviaRulesFragment())
                true
            }
            else -> {
                NavigationUI.onNavDestinationSelected(
                    item,
                    navController
                )
                        || super.onOptionsItemSelected(item)
            }
        }
    }
    fun onPlay() {
        navController.navigate(TriviaTitleFragmentDirections.actionTitleFragmentToGameFragment())
    }
}
