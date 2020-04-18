package com.example.android.androidmarki.ui.home.trivia.gameWon

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.databinding.FragmentTriviaGameWonBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.home.HomeListener

class TriviaGameWonFragment : BaseFragment() {
    private lateinit var binding: FragmentTriviaGameWonBinding
    val clickListener = object : HomeListener.TriviaGameWon {
        override fun onReplay() {
            navController.navigate(TriviaGameWonFragmentDirections.actionGameWonFragmentToGameFragment())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTriviaGameWonBinding.inflate(inflater, container, false).apply {
            gameWon = this@TriviaGameWonFragment
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    private fun getShareIntent(): Intent? {
        val args = TriviaGameWonFragmentArgs.fromBundle(requireArguments())
        return activity?.let {
            ShareCompat.IntentBuilder.from(it)
                .setText(getString(R.string.share_success_text, args.numCorrect, args.numQuestions))
                .setType("text/plain")
                .intent
        }
    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.trivia_winner_menu, menu)
        // check if the activity resolves
        if (null == getShareIntent()?.resolveActivity(requireActivity().packageManager)) {
            // hide the menu item if it doesn't resolve
            menu.findItem(R.id.share)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}
