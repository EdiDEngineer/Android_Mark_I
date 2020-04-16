package com.example.android.androidmarki.ui.main

interface MainListener {
    interface Login{
        fun onReset()
        fun onSignUp()
        fun onSignInWithGoogle()
    }

    interface Verify{
        fun onSendCode()
    }
}