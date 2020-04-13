/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.androidmarki.data.remote.firebase

import android.net.Uri
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import com.example.android.androidmarki.ui.base.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * This class observes the current FirebaseUser. If there is no logged in user, FirebaseUser will
 * be null.
 *
 * Note that onActive() and onInactive() will get triggered when the configuration changes (for
 * example when the device is rotated). This may be undesirable or expensive depending on the
 * nature of your LiveData object.
 */
class FirebaseUserLiveData : LiveData<FirebaseUser?>() {
    private val firebaseAuth = FirebaseAuth.getInstance().apply {
        useAppLanguage()
    }
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        value = firebaseAuth.currentUser
    }
  //later
    val url = "http://www.mark-i-f8a94.firebaseapp.com/login" + firebaseAuth.currentUser?.uid

    private val actionCodeSettings = ActionCodeSettings.newBuilder()
        .setUrl(url)
        .setHandleCodeInApp(true)
        // The default for this is populated with the current android package name.
        .setAndroidPackageName("com.example.android.androidmarki", false, null)
        .build()


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
            linkPhoneNumberWithUserEmail(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Timber.w(e, "onVerificationFailed %t%")

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

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
            this@FirebaseUserLiveData.verificationId = verificationId
            this@FirebaseUserLiveData.token = token
            Timber.d("onCodeSent:$verificationId") // ...
        }
    }

    // When this object has an active observer, start observing the FirebaseAuth state to see if
    // there is currently a logged in user.
    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    // When this object no longer has an active observer, stop observing the FirebaseAuth state to
    // prevent memory leaks.
    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    fun createAccount(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun signIn(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    fun signOut() = firebaseAuth.signOut()

    fun beginVerifyPhoneNumber(baseActivity: BaseActivity, phoneNumber: String) =
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            1L, // Timeout duration
            TimeUnit.MINUTES, // Unit of timeout
            baseActivity, // Activity (for callback binding)
            callback
        )


    fun resendVerificationCode(
        baseActivity: BaseActivity,
        phoneNumber: String
    ) = PhoneAuthProvider.getInstance().verifyPhoneNumber(
        phoneNumber, // Phone number to verify
        60, // Timeout duration
        TimeUnit.SECONDS, // Unit of timeout
        baseActivity, // Activity (for callback binding)
        callback, // OnVerificationStateChangedCallbacks
        token
    ) // ForceResendingToken from callbacks


    fun verifyPhoneNumberWithCode(code: String) =
        PhoneAuthProvider.getCredential(verificationId, code)

    fun linkPhoneNumberWithUserEmail(credential: PhoneAuthCredential) =
        firebaseAuth.currentUser?.linkWithCredential(credential)

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) =
        firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(acct.idToken, null))

    fun verifyEmail() = firebaseAuth.currentUser!!.sendEmailVerification()

    fun deleteUser() = firebaseAuth.currentUser!!.delete()
    fun resetPassword(emailAddress: String) =
        firebaseAuth.sendPasswordResetEmail(emailAddress)

    fun updateProfile(name: String, photoUri: String) = firebaseAuth.currentUser!!.updateProfile(
        UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(Uri.parse(photoUri))
            .build()
    )

    fun updateEmail(email: String) = firebaseAuth.currentUser!!.updateEmail(email)
    fun updatePassword(password: String) = firebaseAuth.currentUser!!.updatePassword(password)

    companion object {
        private lateinit var sInstance: FirebaseUserLiveData

        @MainThread
        fun get(): FirebaseUserLiveData {
            sInstance = if (::sInstance.isInitialized) sInstance else FirebaseUserLiveData()
            return sInstance
        }
    }
}