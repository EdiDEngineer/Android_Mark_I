package com.example.android.androidmarki.ui.main.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.Result
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.data.source.AuthenticateDataSource
import com.example.android.androidmarki.ui.base.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class LoginViewModel(private val repository: AuthenticateRepository) : BaseViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    val loginUIData = LoginUIData()
    val authenticationState = repository.userLiveData.map { user ->
        when {
            user == null -> AuthenticationState.UNAUTHENTICATED
            user.phoneNumber.isNullOrEmpty() -> AuthenticationState.PHONE_UNVERIFIED
            !user.isEmailVerified -> AuthenticationState.EMAIL_UNVERIFIED
            else -> AuthenticationState.AUTHENTICATED
        }
    }

    init {
        _loginResult.value = LoginResult()
    }

    fun login() {
        if (loginUIData.isDataValid) {
            _loginResult.value = LoginResult(
                isLoading = true
            )
            repository.login(
                loginUIData.username.value!!,
                loginUIData.password.value!!,
                object : AuthenticateDataSource.Login {
                    override fun onLoggedIn(Result: Result.Success<Task<AuthResult>>) {
                        Result.data.addOnCompleteListener {
                            if (!it.isSuccessful) {
                                _loginResult.value = LoginResult(
                                    error = R.string.account_failed,
                                    exception = it.exception
                                )
                            } else {
                                _loginResult.value = LoginResult(
                                    isSuccessful = it.isSuccessful,
                                    isLoading = true
                                )
                            }
                        }

                    }

                    override fun onFail(e: Result.Error) {
                        _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                }
            )
        } else {
            _loginResult.value = LoginResult(error = R.string.invalid_details)
        }
    }

    fun loginWithGoogle(account: GoogleSignInAccount) {
        _loginResult.value = LoginResult(
            isLoading = true
        )
        repository.loginWithGoogle(account, object : AuthenticateDataSource.LoginWithGoogle {
            override fun onLoggedIn(Result: Result.Success<Task<AuthResult>>) {
                Result.data.addOnCompleteListener {
                    if (!it.isSuccessful) {
                        _loginResult.value =
                            LoginResult(error = R.string.account_failed, exception = it.exception)
                    } else {
                        _loginResult.value = LoginResult(
                            isSuccessful = it.isSuccessful,
                            isLoading = true
                        )
                    }
                }
            }

            override fun onFail(e: Result.Error) {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
        )
    }


}


