package com.example.android.androidmarki.ui.base

import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import com.example.android.androidmarki.AndroidMarkI

open class BaseViewModel(application: AndroidMarkI = AndroidMarkI.get()) : AndroidViewModel(application) {

//    private val disposable = CompositeDisposable()
//
//    fun fetch(task: () -> Disposable) {
//        disposable.add(task())
//    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
//        disposable.clear()
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, EMAIL_UNVERIFIED, PHONE_UNVERIFIED
    }
//    val userLiveData =  repository.userLiveData
//
//
//    val authenticationState = repository.userLiveData.map { user ->
//        when {
//            user == null -> AuthenticationState.UNAUTHENTICATED
//            user.phoneNumber.isNullOrEmpty()-> AuthenticationState.PHONE_UNVERIFIED
//            !user.isEmailVerified -> AuthenticationState.EMAIL_UNVERIFIED
//            else -> AuthenticationState.AUTHENTICATED
//        }
//    }

//    fun getFactToDisplay(context: Context): String {
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        val factTypePreferenceKey = context.getString(R.string.preference_fact_type_key)
//        val defaultFactType = context.resources.getStringArray(R.array.fact_type)[0]
//        val funFactType = sharedPreferences.getString(factTypePreferenceKey, defaultFactType)
//
//        if (authenticationState == AuthenticationState.UNAUTHENTICATED || funFactType.equals(
//                context.getString(R.string.fact_type_android)
//            )
//        ) {
//            return androidFacts[Random.nextInt(0, androidFacts.size)]
//        } else {
//            return californiaFacts[Random.nextInt(0, californiaFacts.size)]
//        }
//    }
}