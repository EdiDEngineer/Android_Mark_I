package com.example.android.androidmarki.ui.home.trivia.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.androidmarki.R
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.home.HomeListener

class TriviaRulesFragment : BaseFragment() {

    private lateinit var triviaRulesViewModel: TriviaRulesViewModel
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
        triviaRulesViewModel =
            ViewModelProviders.of(this).get(TriviaRulesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_trivia_title, container, false)
//        val textView: TextView = root.findViewById(R.id.nav_trivia)
        triviaRulesViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })
        return root
    }
}
