package com.example.android.androidmarki.ui.main.verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.Result
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.data.source.AuthenticateDataSource
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.base.BaseViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential

class VerifyViewModel(private val repository: AuthenticateRepository) : BaseViewModel() {
    private val _verifyResult = MutableLiveData<VerifyResult>()
    val verifyResult: LiveData<VerifyResult> = _verifyResult
    val verifyUIData = VerifyUIData()

    init {
        _verifyResult.value = VerifyResult()
    }

    fun verify() {
        if (verifyUIData.isDataValid) {
            repository.verifyPhoneNumberWithCode(verifyUIData.code.value!!,
                object : AuthenticateDataSource.VerifyPhoneNumber {
                    override fun onVerify(credential: PhoneAuthCredential) {
                        linkPhoneNumber(credential)
                    }
                })

        } else {
            _verifyResult.value = VerifyResult(error = R.string.invalid_verify_info)
        }
    }

    fun sendCode(baseActivity: BaseActivity) {
        if (verifyUIData.phoneNumberError.value == 0) {
            if (verifyUIData.isCode.value == true) {
                repository.resendVerificationCode(
                    baseActivity,
                    verifyUIData.phoneNumber.value!!,
                    object : AuthenticateDataSource.SendPhoneNumberCode {
                        override fun onSentCode() {
                        }
                    }
                )
            } else {
                repository.beginVerifyPhoneNumber(
                    baseActivity,
                    verifyUIData.phoneNumber.value!!,
                    object : AuthenticateDataSource.SendPhoneNumberCode {
                        override fun onSentCode() {
                            verifyUIData.isCode.value = true
                        }
                    }
                )
            }

        } else {
            _verifyResult.value = VerifyResult(error = R.string.invalid_phone_number)
        }
    }

    fun signOut() {
        repository.logout(object : AuthenticateDataSource.Logout {
            override fun onLoggedOut() {
                _verifyResult.value = VerifyResult(isSignOut = true)
            }
        })
    }

   private fun linkPhoneNumber(credential: PhoneAuthCredential) {
        repository.linkPhoneNumberWithUserEmail(
            credential,
            object : AuthenticateDataSource.LinkPhoneNumber {
                override fun onLinked(Result: Result.Success<Task<AuthResult>>) {
                 Result.data.addOnCompleteListener {
                     if(!it.isSuccessful){
                         _verifyResult.value = VerifyResult(error = R.string.verification_failed, exception = it.exception)
                     }
                     else{
                         _verifyResult.value = VerifyResult(isSuccessful = true, isLoading = MutableLiveData(true))
                     }
                 }
                }

                override fun onFail(e: Result.Error) {
                }

            })
    }
}
