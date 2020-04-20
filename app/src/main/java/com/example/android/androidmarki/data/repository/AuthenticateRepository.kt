package com.example.android.androidmarki.data.repository

import androidx.annotation.MainThread
import androidx.lifecycle.map
import com.example.android.androidmarki.data.Result
import com.example.android.androidmarki.data.remote.firebase.FirebaseUserLiveData
import com.example.android.androidmarki.data.source.AuthenticateDataSource
import com.example.android.androidmarki.ui.base.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.PhoneAuthCredential

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class AuthenticateRepository(private val userLiveData: FirebaseUserLiveData = FirebaseUserLiveData.get()) {

    fun login(username: String, password: String, callBack: AuthenticateDataSource.Login) {
        try {

            callBack.onLoggedIn(
                Result.Success(
                    userLiveData.signIn(
                        username,
                        password
                    )
                )
            )
        } catch (e: Exception) {
            callBack.onFail(
                Result.Error(
                    Exception(
                        "Error logging in",
                        e
                    )
                )
            )
        }
    }

    fun logout(callBack: AuthenticateDataSource.Logout) {
    userLiveData.signOut()
        callBack.onLoggedOut()
    }

    fun signUp(username: String, password: String, callBack: AuthenticateDataSource.SignUp) {
        try {
            callBack.onSignedUp(
                Result.Success(
                    userLiveData.createAccount(
                        username,
                        password
                    )
                )
            )
        } catch (e: Exception) {
            callBack.onFail(
                Result.Error(
                    Exception(
                        "Error signing up",
                        e
                    )
                )
            )
        }
    }

    fun beginVerifyPhoneNumber(
        baseActivity: BaseActivity,
        phoneNumber: String,
        callback: AuthenticateDataSource.SendPhoneNumberCode
    ) {

        userLiveData.beginVerifyPhoneNumber(baseActivity, phoneNumber)
        callback.onSentCode()
    }

    fun resendVerificationCode(
        baseActivity: BaseActivity,
        phoneNumber: String,
        callback: AuthenticateDataSource.SendPhoneNumberCode
    ) {
        userLiveData.resendVerificationCode(baseActivity, phoneNumber)
        callback.onSentCode()

    }

    fun verifyPhoneNumberWithCode(
        code: String,
        callback: AuthenticateDataSource.VerifyPhoneNumber
    ) {
        callback.onVerify(userLiveData.verifyPhoneNumberWithCode(code))
    }

    fun linkPhoneNumberWithUserEmail(
        credential: PhoneAuthCredential,
        callback: AuthenticateDataSource.LinkPhoneNumber
    ) {
        try {
            callback.onLinked(
                Result.Success(
                    userLiveData.linkPhoneNumberWithUserEmail(
                        credential
                    )!!
                )
            )
        } catch (e: Exception) {
            callback.onFail(
                Result.Error(
                    Exception(
                        "Error linking phone with email",
                        e
                    )
                )
            )
        }

    }

    fun loginWithGoogle(
        acct: GoogleSignInAccount,
        callback: AuthenticateDataSource.LoginWithGoogle
    ) {
        try {
            callback.onLoggedIn(
                Result.Success(
                    userLiveData.firebaseAuthWithGoogle(
                        acct
                    )
                )
            )
        } catch (e: Exception) {
            callback.onFail(
                Result.Error(
                    Exception(
                        "Error signing up",
                        e

                    )
                )
            )
        }
    }


    fun verifyEmail(callback: AuthenticateDataSource.VerifyEmail) {
        try {
            callback.onVerify(Result.Success(userLiveData.verifyEmail()))
        } catch (e: Exception) {
            callback.onFail(
                Result.Error(
                    Exception(
                        "Error verifying  email",
                        e
                    )
                )
            )
        }
    }

    fun deleteUser(callback: AuthenticateDataSource.DeleteUser) {
        try {
            callback.onDeleted(Result.Success(userLiveData.deleteUser()))
        } catch (e: Exception) {
            callback.onFail(
                Result.Error(
                    Exception(
                        "Error verifying  email",
                        e
                    )
                )
            )
        }
    }

    fun resetPassword(emailAddress: String, callback: AuthenticateDataSource.ResetPassword) {
        try {
            callback.onReset(
                Result.Success(
                    userLiveData.resetPassword(
                        emailAddress
                    )
                )
            )

        } catch (e: Exception) {
            callback.onFail(
                Result.Error(
                    Exception(
                        "Error verifying  email",
                        e
                    )
                )
            )
        }
    }

    val authenticationState = userLiveData.map { user ->
        when {
            user == null -> AuthenticationState.UNAUTHENTICATED
            user.phoneNumber.isNullOrEmpty() -> AuthenticationState.PHONE_UNVERIFIED
            !user.isEmailVerified -> AuthenticationState.EMAIL_UNVERIFIED
            else -> AuthenticationState.AUTHENTICATED
        }
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, EMAIL_UNVERIFIED, PHONE_UNVERIFIED
    }

    companion object {
        private lateinit var sInstance: AuthenticateRepository

        @MainThread
        fun get(): AuthenticateRepository {
            sInstance = if (::sInstance.isInitialized) sInstance else AuthenticateRepository()
            return sInstance
        }
    }
}
