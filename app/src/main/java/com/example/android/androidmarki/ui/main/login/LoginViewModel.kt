package com.example.android.androidmarki.ui.main.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.ui.base.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: AuthenticateRepository) : BaseViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    var loginUIData = LoginUIData()
    val authenticationState = repository.authenticationState

    init {
        _loginResult.value = LoginResult()
    }

    fun login() {
        if (loginUIData.isDataValid) {
            _loginResult.value = LoginResult(
                isLoading = true
            )
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        repository.signIn(
                            loginUIData.username.value!!,
                            loginUIData.password.value!!
                        ).await()
                    } catch (e: FirebaseNetworkException) {
                        _loginResult.postValue(
                            LoginResult(
                                error = R.string.network_error,
                                exception = e
                            )
                        )
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        _loginResult.postValue(
                            LoginResult(
                                error = R.string.details_error,
                                exception = e
                            )
                        )
                    } catch (e: Exception) {
                        _loginResult.postValue(
                            LoginResult(
                                error = R.string.account_failed,
                                exception = e
                            )
                        )
                    }
                }
            }
        } else {
            _loginResult.value = LoginResult(error = R.string.invalid_details)
        }
    }

    fun loginWithGoogle(account: GoogleSignInAccount) {
        _loginResult.value = LoginResult(
            isLoading = true
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    repository.signInWithGoogle(
                        account
                    ).await()
                } catch (e: FirebaseNetworkException) {
                    _loginResult.postValue(
                        LoginResult(
                            error = R.string.network_error,
                            exception = e
                        )
                    )
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    _loginResult.postValue(
                        LoginResult(
                            error = R.string.details_error,
                            exception = e
                        )
                    )
                } catch (e: Exception) {
                    _loginResult.postValue(
                        LoginResult(
                            error = R.string.account_failed,
                            exception = e
                        )
                    )
                }
            }
        }
    }


    fun clear() {
        _loginResult.value = LoginResult()
    }

    fun validate() {
        loginUIData.isDataValid =
            loginUIData.passwordError.value == null && loginUIData.usernameError.value == null
    }
}


