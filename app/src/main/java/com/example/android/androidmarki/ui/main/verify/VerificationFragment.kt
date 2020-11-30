package com.example.android.androidmarki.ui.main.verify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.data.repository.AuthenticationState
import com.example.android.androidmarki.databinding.FragmentVerificationBinding
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory
import com.example.android.androidmarki.ui.main.MainListener
import timber.log.Timber

class VerificationFragment : BaseFragment() {
    private lateinit var binding: FragmentVerificationBinding
    val clickListener = object : MainListener.Verify {
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
                    VerifyViewModel(AuthenticateRepository())
                )
            ).get(
                VerifyViewModel::class.java
            )
            verify = this@VerificationFragment
            lifecycleOwner = viewLifecycleOwner
        }
        layoutView = binding.root
        return layoutView
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

        binding.viewModel!!.verifyResult.observe(viewLifecycleOwner, Observer {
            if (it.error != null) {
                showSnackBar(it.error)
                Timber.tag(TAG).w(it.exception, "linkUserWithPhoneNumber:failure")
                binding.viewModel!!.clear()

            }
            if (it.isSuccessful) {
                showShortToast(R.string.phone_success)
                navController.navigate(R.id.home_activity)
                requireActivity().finish()
            }
        })

        binding.viewModel!!.authenticationState.observe(viewLifecycleOwner, Observer {
            if (it == AuthenticationState.UNAUTHENTICATED){
                navController.navigate(VerificationFragmentDirections.actionVerificationFragmentToLoginFragment())
            }
        })


        binding.verifyPhoneNo.doAfterTextChanged{
            binding.viewModel!!.validate()
        }
        binding.verifyCode.doAfterTextChanged{
            binding.viewModel!!.validate()
        }
    }

    companion object {
        const val TAG = "Verify Fragment"
    }
}