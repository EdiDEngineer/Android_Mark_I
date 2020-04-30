package com.example.android.androidmarki.data.repository

import androidx.lifecycle.map
import com.example.android.androidmarki.R
import com.example.android.androidmarki.data.remote.firebase.FirebaseUserAuthLiveData
import com.example.android.androidmarki.data.source.AuthenticateDataSource
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.main.verify.VerifyResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
enum class AuthenticationState {
    AUTHENTICATED, UNAUTHENTICATED, EMAIL_UNVERIFIED, PHONE_UNVERIFIED
}

class AuthenticateRepository(private val userAuthLiveData: FirebaseUserAuthLiveData = FirebaseUserAuthLiveData()) :
    AuthenticateDataSource {

    val authenticationState = userAuthLiveData.map { user ->
        when {
            user == null -> AuthenticationState.UNAUTHENTICATED
            user.phoneNumber.isNullOrEmpty() -> AuthenticationState.PHONE_UNVERIFIED
            !user.isEmailVerified -> AuthenticationState.EMAIL_UNVERIFIED
            else -> AuthenticationState.AUTHENTICATED
        }
    }
    private lateinit var verificationId: String
    private lateinit var token: PhoneAuthProvider.ForceResendingToken

    private val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Timber.d("onVerificationCompleted:$credential")

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Timber.w(e, "onVerificationFailed %t%")
                // Show a message and update the UI
                // ...


            // Show a message and update the UI
            // ...
        }


        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            this@AuthenticateRepository.verificationId = verificationId
            this@AuthenticateRepository.token = token
            Timber.d("onCodeSent:$verificationId") // ...
        }
    }


    override suspend fun signInWithGoogle(acct: GoogleSignInAccount) =
        userAuthLiveData.signInWithGoogle(acct)

    override suspend fun createAccount(email: String, password: String) =
        userAuthLiveData.createAccount(email, password)

    override suspend fun signIn(email: String, password: String) =
        userAuthLiveData.signIn(email, password)

    override fun signOut() = userAuthLiveData.signOut()
    override suspend fun beginVerifyPhoneNumber(
        baseActivity: BaseActivity,
        phoneNumber: String) = PhoneAuthProvider.getInstance().verifyPhoneNumber(
        phoneNumber, // Phone number to verify
        1L, // Timeout duration
        TimeUnit.MINUTES, // Unit of timeout
        baseActivity, // Activity (for callback binding)
        callback
    )

    override suspend fun resendVerificationCode(
        baseActivity: BaseActivity,
        phoneNumber: String
    ) = PhoneAuthProvider.getInstance().verifyPhoneNumber(
        phoneNumber, // Phone number to verify
        60, // Timeout duration
        TimeUnit.SECONDS, // Unit of timeout
        baseActivity, // Activity (for callback binding)
        callback, // OnVerificationStateChangedCallbacks
        token
    )

    override fun verifyPhoneNumberWithCode(
        code: String
    ) = PhoneAuthProvider.getCredential(verificationId, code)

    override suspend fun linkPhoneNumberWithUserEmail(credential: PhoneAuthCredential) =
        userAuthLiveData.value!!.linkWithCredential(credential)

    override suspend fun verifyEmail() = userAuthLiveData.value!!.sendEmailVerification()

    override suspend fun resetPassword(emailAddress: String) =
        userAuthLiveData.resetPassword(emailAddress)

}

