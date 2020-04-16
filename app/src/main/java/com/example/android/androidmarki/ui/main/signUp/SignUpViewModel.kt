package com.example.android.androidmarki.ui.main.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.Result
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.data.source.AuthenticateDataSource
import com.example.android.androidmarki.ui.base.BaseViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class SignUpViewModel(private val repository: AuthenticateRepository) : BaseViewModel() {

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult
    val signUpUIData = SignUpUIData()

    init {
        _signUpResult.value = SignUpResult()
    }

    fun signUp() {
        if (signUpUIData.isDataValid) {
            _signUpResult.value = SignUpResult(isLoading = true)
            repository.signUp(
                signUpUIData.username.value!!,
                signUpUIData.password.value!!,
                object : AuthenticateDataSource.SignUp {
                    override fun onSignedUp(Result: Result.Success<Task<AuthResult>>) {
                        Result.data.addOnCompleteListener {
                            if (!it.isSuccessful) {
                                _signUpResult.value = SignUpResult(error = R.string.account_failed, exception = it.exception)
                            } else {
                                verifyEmail()
                            }
                        }
                    }

                    override fun onFail(e: Result.Error) {
                        _signUpResult.value = SignUpResult(error = R.string.signUp_failed)
                    }
                }
            )
        } else {
            _signUpResult.value = SignUpResult(error = R.string.invalid_details)
        }
    }

    private fun verifyEmail() {
        repository.verifyEmail(object : AuthenticateDataSource.VerifyEmail {
            override fun onVerify(Result: Result.Success<Task<Void>>) {
                Result.data.addOnCompleteListener {
                    if (!it.isSuccessful) {
                        _signUpResult.value = SignUpResult(error = R.string.verify_email_failed, exception = it.exception)
                    } else {
                        _signUpResult.value = SignUpResult(
                            isSuccessful = it.isSuccessful, isLoading = true
                        )
                    }
                }
            }

            override fun onFail(e: Result.Error) {
                _signUpResult.value = SignUpResult(error = R.string.verify_email_failed)
            }

        })

    }


}