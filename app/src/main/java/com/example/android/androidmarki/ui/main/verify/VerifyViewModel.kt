package com.example.android.androidmarki.ui.main.verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.base.BaseViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class VerifyViewModel(private val repository: AuthenticateRepository) : BaseViewModel() {
    private val _verifyResult = MutableLiveData<VerifyResult>()
    val verifyResult: LiveData<VerifyResult> = _verifyResult
    val verifyUIData = VerifyUIData()
    val authenticationState = repository.authenticationState

    init {
        _verifyResult.value = VerifyResult()
    }

    fun verify() {
        _verifyResult.value = VerifyResult(isLoading = true)
        if (verifyUIData.isDataValid) {
            linkPhoneNumber(
                repository.verifyPhoneNumberWithCode(
                    verifyUIData.code.value!!
                )
            )
        } else {
            _verifyResult.value = VerifyResult(error = R.string.invalid_verify_info)
        }
    }

    fun sendCode(baseActivity: BaseActivity) {
        if (verifyUIData.phoneNumberError.value == null && verifyUIData.phoneNumber.value!=null) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (verifyUIData.isCode.value == true) {
                        repository.resendVerificationCode(
                            baseActivity,
                            verifyUIData.phoneNumber.value!!
                        )
                    } else {
                        repository.beginVerifyPhoneNumber(
                            baseActivity,
                            verifyUIData.phoneNumber.value!!
                        )
                        verifyUIData.isCode.postValue(true)
                    }
                }
            }

        } else {
            _verifyResult.value = VerifyResult(error = R.string.invalid_phone_number)
        }
    }

    fun signOut() {
        repository.signOut()
    }

    private fun linkPhoneNumber(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    repository.linkPhoneNumberWithUserEmail(
                        credential
                    ).await()
                    _verifyResult.postValue(VerifyResult(isSuccessful = true, isLoading = true))
                } catch (e: FirebaseNetworkException) {
                    _verifyResult.postValue(VerifyResult(
                        error = R.string.network_error,
                        exception = e
                    ))
                } catch (e: FirebaseAuthInvalidCredentialsException){
                    _verifyResult.postValue(VerifyResult(
                        error = R.string.details_error_phone,
                        exception = e
                    ))
                }catch (e: Exception) {
                    _verifyResult.postValue( VerifyResult(
                        error = R.string.number_link_failed,
                        exception = e
                    ))
                }
            }
        }

    }

    fun clear() {
        _verifyResult.value = VerifyResult()
    }

    fun validate() {
        verifyUIData.isDataValid =
            verifyUIData.phoneNumberError.value == null && verifyUIData.codeError.value == null
    }
}
