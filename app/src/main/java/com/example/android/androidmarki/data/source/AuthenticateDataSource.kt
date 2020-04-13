package com.example.android.androidmarki.data.source

import com.example.android.androidmarki.data.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
interface AuthenticateDataSource  {
    interface Login   {
        fun onLoggedIn(Result: Result.Success<Task<AuthResult>>)
        fun onFail(e: Result.Error)
    }

    interface Logout   {
        fun onLoggedOut()
    }

    interface SignUp   {
        fun onSignedUp(Result: Result.Success<Task<AuthResult>>)
        fun onFail(e: Result.Error)
    }

    interface SendPhoneNumberCode   {
        fun onSentCode()
    }
    interface VerifyPhoneNumber   {
        fun onVerify(credential: PhoneAuthCredential)
    }

    interface LinkPhoneNumber   {
        fun onLinked(Result: Result.Success<Task<AuthResult>>)
        fun onFail(e: Result.Error)
    }

    interface LoginWithGoogle   {
        fun onLoggedIn(Result: Result.Success<Task<AuthResult>>)
        fun onFail(e: Result.Error)
    }

    interface VerifyEmail   {
        fun onVerify(Result: Result.Success<Task<Void>>)
        fun onFail(e: Result.Error)
    }

    interface DeleteUser   {
        fun onDeleted(Result: Result.Success<Task<Void>>)
        fun onFail(e: Result.Error)
    }

    interface ResetPassword   {
        fun onReset(Result: Result.Success<Task<Void>>)
        fun onFail(e: Result.Error)
    }
}

