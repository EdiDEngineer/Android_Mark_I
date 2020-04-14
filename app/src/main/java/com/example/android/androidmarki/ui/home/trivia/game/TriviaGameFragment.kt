package com.example.android.androidmarki.ui.home.trivia.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.androidmarki.R
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.home.HomeListener

class TriviaGameFragment : BaseFragment() {

    private lateinit var triviaGameViewModel: TriviaGameViewModel
    private val clickListener = object : HomeListener.TriviaTitle {
        override fun onPlay() {
//            findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        triviaGameViewModel =
            ViewModelProviders.of(this).get(TriviaGameViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_trivia_title, container, false)
//        val textView: TextView = root.findViewById(R.id.nav_trivia)
        triviaGameViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })
        return root
    }
}
