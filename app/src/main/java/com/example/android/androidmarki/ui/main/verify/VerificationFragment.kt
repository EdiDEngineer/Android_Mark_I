package com.example.android.androidmarki.ui.main.verify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.databinding.FragmentVerificationBinding
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory
import com.example.android.androidmarki.ui.main.MainListener
import timber.log.Timber

class VerificationFragment : BaseFragment() {
    private lateinit var binding: FragmentVerificationBinding
    private val clickListener = object : MainListener.Verify {
        override fun onBack() {
            requireActivity().onBackPressed()
        }

        override fun onSendCode() {
            binding.viewModel!!.sendCode(requireActivity() as BaseActivity)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVerificationBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProvider(
                this@VerificationFragment, BaseViewModelFactory(
                    VerifyViewModel(AuthenticateRepository.get())
                )
            ).get(
                VerifyViewModel::class.java
            )
            clickListener = this@VerificationFragment.clickListener
            lifecycleOwner = viewLifecycleOwner
        }
        snackView = binding.root
        return snackView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showDialog(
                "Exit Verification",
                "If you exit you will be logged out \nDo you want to exit ?",
                positiveAction = { _, _ ->
                    binding.viewModel!!.signOut()
                })
        }
binding.verify.setOnClickListener {  }

        binding.viewModel!!.verifyResult.observe(viewLifecycleOwner, Observer {
            if (it.error != null) {
                showSnackBar(it.error!!)
                it.error = null
                Timber.tag(TAG).w(it.exception, "linkUserWithPhoneNumber:failure")
                it.exception = null

            }
            if (it.isSuccessful) {
                showShortToast(R.string.phone_success)
                navController.navigate(R.id.home_activity)
                requireActivity().finish()
            }
            if (it.isSignOut) {
                navController.popBackStack(R.id.loginFragment, false)
            }
        })

        var phoneNumberValid = binding.viewModel!!.verifyUIData.isDataValid
        var codeValid = binding.viewModel!!.verifyUIData.isDataValid
        binding.viewModel!!.verifyUIData.phoneNumberError.observe(viewLifecycleOwner, Observer {
            phoneNumberValid = it == 0
            binding.viewModel!!.verifyUIData.isDataValid = phoneNumberValid && codeValid
        })
        binding.viewModel!!.verifyUIData.codeError.observe(viewLifecycleOwner, Observer {
            codeValid = it == 0
            binding.viewModel!!.verifyUIData.isDataValid = phoneNumberValid && codeValid

        })

    }

    companion object {
        const val TAG = "Verify Fragment"
    }
}