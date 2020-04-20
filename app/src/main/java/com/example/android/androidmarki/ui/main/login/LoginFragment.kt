package com.example.android.androidmarki.ui.main.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.databinding.FragmentLoginBinding
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.base.BaseViewModel
import com.example.android.androidmarki.ui.base.BaseViewModelFactory
import com.example.android.androidmarki.ui.main.MainListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import timber.log.Timber


class LoginFragment : BaseFragment() {
    private lateinit var binding: FragmentLoginBinding
    val clickListener = object : MainListener.Login {
        override fun onReset() {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToResetFragment())
        }

        override fun onSignUp() {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }

        override fun onSignInWithGoogle() {
            val signInIntent = GoogleSignIn.getClient(
                requireActivity(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            ).signInIntent
            startActivityForResult(signInIntent, SIGN_IN_RESULT_CODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProvider(
                this@LoginFragment, BaseViewModelFactory(
                    LoginViewModel(AuthenticateRepository.get())
                )
            ).get(
                LoginViewModel::class.java
            )
            login = this@LoginFragment
            lifecycleOwner = viewLifecycleOwner
        }
        layoutView = binding.root
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.viewModel!!.authenticationState.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                if (authenticationState == AuthenticateRepository.AuthenticationState.AUTHENTICATED || authenticationState == AuthenticateRepository.AuthenticationState.EMAIL_UNVERIFIED) {
                    showShortToast(R.string.login_successful)
                    navController.navigate(R.id.home_activity)
                    requireActivity().finish()

                } else if (authenticationState == AuthenticateRepository.AuthenticationState.PHONE_UNVERIFIED) {
                    showShortToast(R.string.login_successful)
                    navController.navigate(R.id.verificationFragment)
                }

            })

        binding.viewModel!!.loginResult.observe(viewLifecycleOwner, Observer {
            if (it.error != null) {
                showSnackBar(it.error)
                Timber.tag(TAG).w(it.exception, "signInWithEmail:failure")
                binding.viewModel!!.clear()
            }

        })

        var passwordValid = binding.viewModel!!.loginUIData.isDataValid
        var usernameValid = binding.viewModel!!.loginUIData.isDataValid
        binding.viewModel!!.loginUIData.passwordError.observe(viewLifecycleOwner, Observer {
            passwordValid = it == 0
            binding.viewModel!!.loginUIData.isDataValid = passwordValid && usernameValid
        })
        binding.viewModel!!.loginUIData.usernameError.observe(viewLifecycleOwner, Observer {
            usernameValid = it == 0
            binding.viewModel!!.loginUIData.isDataValid = passwordValid && usernameValid

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                binding.viewModel!!.loginWithGoogle(
                    task.getResult(ApiException::class.java)!!
                )

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.tag(TAG).w(e, "Google sign in failed")
                // ...
            }
        }
    }

    companion object {
        const val SIGN_IN_RESULT_CODE = 1001
        const val TAG = "Login Fragment"
    }
}