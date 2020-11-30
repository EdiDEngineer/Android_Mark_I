package com.example.android.androidmarki.ui.main.reset

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

class ResetViewModel(private val repository: AuthenticateRepository) : BaseViewModel() {
    private val _resetResult = MutableLiveData<ResetResult>()
    val resetResult: LiveData<ResetResult> = _resetResult
    val resetUIData = ResetUIData()

    init {
        _resetResult.value = ResetResult()
    }

    fun reset() {
        if (resetUIData.isDataValid) {
            _resetResult.value = ResetResult(isLoading = true)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        repository.resetPassword(resetUIData.username.value!!).await()
                        _resetResult.postValue(ResetResult(isSuccessful = true, isLoading = true))
                    } catch (e: FirebaseNetworkException) {
                        _resetResult.postValue(
                            ResetResult(
                                error = R.string.network_error,
                                exception = e
                            )
                        )
                    } catch (e: Exception) {
                        _resetResult.postValue(
                            ResetResult(
                                error = R.string.details_error,
                                exception = e
                            )
                        )
                    }
                }
            }
        } else {
            _resetResult.value = ResetResult(error = R.string.invalid_details)
        }

    }

    fun clear() {
        _resetResult.value = ResetResult()
    }

    fun validate() {
        resetUIData.isDataValid = resetUIData.usernameError.value == null
    }

}