package com.example.android.androidmarki.data.source

import com.example.android.androidmarki.ui.base.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
interface AuthenticateDataSource {

    suspend fun signInWithGoogle(acct: GoogleSignInAccount): Task<AuthResult>

    suspend fun createAccount(email: String, password: String): Task<AuthResult>

    suspend fun signIn(email: String, password: String): Task<AuthResult>

    fun signOut()

   suspend fun beginVerifyPhoneNumber(
        baseActivity: BaseActivity,
        phoneNumber: String)

    suspend  fun resendVerificationCode(
        baseActivity: BaseActivity,
        phoneNumber: String
    )

    fun verifyPhoneNumberWithCode( code: String): PhoneAuthCredential

    suspend fun linkPhoneNumberWithUserEmail(credential: PhoneAuthCredential): Task<AuthResult>

    suspend fun verifyEmail(): Task<Void>

    suspend fun resetPassword(emailAddress: String): Task<Void>

}

