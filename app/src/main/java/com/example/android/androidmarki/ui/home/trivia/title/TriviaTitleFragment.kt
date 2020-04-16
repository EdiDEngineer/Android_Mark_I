package com.example.android.androidmarki.ui.home.trivia.title

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentTriviaTitleBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.home.HomeListener

class TriviaTitleFragment : BaseFragment() {

    private lateinit var binding: FragmentTriviaTitleBinding
    val clickListener = object : HomeListener.TriviaTitle {
        override fun onPlay() {
            navController.navigate(TriviaTitleFragmentDirections.actionTitleFragmentToGameFragment())
        }
    }

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
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        )
                || super.onOptionsItemSelected(item)
    }

}
