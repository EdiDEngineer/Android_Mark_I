package com.example.android.androidmarki.ui.main.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.ui.base.BaseViewModel
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpViewModel(private val repository: AuthenticateRepository) : BaseViewModel() {

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult
    var signUpUIData = SignUpUIData()
val authenticationState = repository.authenticationState
    init {
        _signUpResult.value = SignUpResult()
    }

    fun signUp() {
        if (signUpUIData.isDataValid) {
            _signUpResult.value = SignUpResult(isLoading = true)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        repository.createAccount(
                            signUpUIData.username.value!!,
                            signUpUIData.password.value!!
                        ).await()
                    }
                    catch (e: FirebaseNetworkException) {
                        _signUpResult.postValue( SignUpResult(
                            error = R.string.network_error,
                            exception = e
                        ))
                    } catch (e: Exception) {
                        _signUpResult.postValue(SignUpResult(
                            error = R.string.account_failed,
                            exception = e
                        ))
                    }

                }
            }

        } else {
            _signUpResult.value = SignUpResult(error = R.string.invalid_details)
        }
    }

   fun verifyEmail() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    repository.verifyEmail().await()
                    _signUpResult.postValue(SignUpResult(
                        isSuccessful = true, isLoading = true
                    ))
                } catch (e: FirebaseNetworkException) {
                    _signUpResult.postValue( SignUpResult(
                        error = R.string.network_error,
                        exception = e
                    ))
                } catch (e: Exception) {
                    _signUpResult.postValue( SignUpResult(
                        error = R.string .account_failed,
                        exception = e
                    ))
                }
            }
        }

    }


    fun clear() {
        _signUpResult.value = SignUpResult()
    }

    fun validate() {
        signUpUIData.isDataValid =
            signUpUIData.usernameError.value == null && signUpUIData.passwordError.value == null
    }

}