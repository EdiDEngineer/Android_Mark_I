package com.example.android.androidmarki.ui.main.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.Result
import com.example.android.androidmarki.data.repository.AuthenticateRepository
import com.example.android.androidmarki.data.source.AuthenticateDataSource
import com.example.android.androidmarki.ui.base.BaseViewModel
import com.google.android.gms.tasks.Task

class ResetViewModel(private val repository: AuthenticateRepository) : BaseViewModel() {
    private val _resetResult = MutableLiveData<ResetResult>()
    val resetResult: LiveData<ResetResult> = _resetResult
    val resetUIData = ResetUIData()

    init {
        _resetResult.value = ResetResult()
    }

    fun reset() {
        if (resetUIData.usernameError.value == 0) {
            _resetResult.value!!.isLoading.value = true
            repository.resetPassword(
                resetUIData.username.value!!,
                object : AuthenticateDataSource.ResetPassword {
                    override fun onReset(Result: Result.Success<Task<Void>>) {
                        Result.data.addOnCompleteListener {
                            if (!it.isSuccessful) {
                                _resetResult.value = ResetResult(error = R.string.account_failed, exception =  it.exception)
                            } else {
                                _resetResult.value = ResetResult(isSuccessful = true, isLoading = MutableLiveData(true))
                            }
                        }
                    }

                    override fun onFail(e: Result.Error) {
                        _resetResult.value = ResetResult(error = R.string.login_failed)
                    }
                }
            )
        } else {
            _resetResult.value = ResetResult(error = R.string.invalid_details)
        }

    }

}