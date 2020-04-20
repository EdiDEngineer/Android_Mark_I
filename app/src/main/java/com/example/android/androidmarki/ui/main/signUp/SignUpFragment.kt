package com.example.android.androidmarki.ui.main.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.repository.AuthenticateRepository
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
                    SignUpViewModel(AuthenticateRepository.get())
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
                navController.navigate(R.id.verificationFragment)
            }
        })

        var passwordValid = binding.viewModel!!.signUpUIData.isDataValid
        var usernameValid = binding.viewModel!!.signUpUIData.isDataValid
        binding.viewModel!!.signUpUIData.passwordError.observe(viewLifecycleOwner, Observer {
            passwordValid = it == 0
            binding.viewModel!!.signUpUIData.isDataValid = passwordValid && usernameValid
        })
        binding.viewModel!!.signUpUIData.usernameError.observe(viewLifecycleOwner, Observer {
            usernameValid = it == 0
            binding.viewModel!!.signUpUIData.isDataValid = passwordValid && usernameValid

        })
    }

    companion object {
        const val TAG = "Sign Up Fragment"
    }

}