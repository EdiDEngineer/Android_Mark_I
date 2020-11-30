package com.example.android.androidmarki.ui.main.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.data.repository.AuthenticationState
import com.example.android.androidmarki.databinding.FragmentSignUpBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModelFactory
import timber.log.Timber


class SignUpFragment : BaseFragment() {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProvider(
                this@SignUpFragment, BaseViewModelFactory(
                    SignUpViewModel(AuthenticateRepository())
                )
            ).get(
                SignUpViewModel::class.java
            )

            signUp = this@SignUpFragment
            lifecycleOwner = viewLifecycleOwner
        }
        layoutView = binding.root
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.viewModel!!.signUpResult.observe(viewLifecycleOwner, Observer {
            if (it.error != null) {
                showSnackBar(it.error)
                Timber.tag(TAG).w(it.exception, "createUserWithEmail:failure")
                binding.viewModel!!.clear()
            }
            if (it.isSuccessful) {
                showShortToast(R.string.sign_up_success)
                navController.navigate(R.id.action_global_verificationFragment)
            }
        })
        binding.viewModel!!.authenticationState.observe(viewLifecycleOwner, Observer {
            if (it == AuthenticationState.PHONE_UNVERIFIED) {
                binding.viewModel!!.verifyEmail()
            }
        })
        binding.signUpUsername.doAfterTextChanged {
            binding.viewModel!!.validate()
        }
        binding.signUpPassword.doAfterTextChanged {
            binding.viewModel!!.validate()
        }
    }

    fun onLogin() {
        navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())

    }

    companion object {
        const val TAG = "Sign Up Fragment"
    }

}