package com.example.android.androidmarki.ui.main.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.databinding.FragmentResetBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory
import timber.log.Timber

class ResetFragment : BaseFragment() {
    private lateinit var binding: FragmentResetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProvider(
                this@ResetFragment, BaseViewModelFactory(
                    ResetViewModel(AuthenticateRepository.get())
                )
            ).get(
                ResetViewModel::class.java
            )
            reset = this@ResetFragment
            lifecycleOwner = viewLifecycleOwner
        }
        layoutView = binding.root
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.viewModel!!.resetResult.observe(viewLifecycleOwner, Observer {
            if (it.error != null) {
                showSnackBar(it.error!!)
                it.error = null
                Timber.tag(TAG).w(it.exception, "createUserWithEmail:failure")
                it.exception = null
            }
            if (it.isSuccessful) {
                showShortToast(R.string.password_reset_email_error)
                navController.popBackStack(R.id.loginFragment, false)
            }
        })
    }

    companion object {
        const val TAG = "Reset Fragment"
    }


}