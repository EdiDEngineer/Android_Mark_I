package com.example.android.androidmarki.ui.main

interface MainListener {
    interface Login{
        fun onReset()
        fun onSignUp()
        fun onSignInWithGoogle()
    }
    interface Reset{
        fun onBack()
    }

    interface SignUp{
        fun onBack()
        fun onLogin()
    }

    interface Verify{
        fun onBack()
        fun onSendCode()
    }
}