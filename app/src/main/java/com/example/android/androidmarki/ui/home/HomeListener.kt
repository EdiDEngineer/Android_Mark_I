package com.example.android.androidmarki.ui.home

interface HomeListener {
    interface TriviaTitle{
        fun onPlay()
    }
    interface TriviaGameOver{
        fun onRetry()
    }
    interface TriviaGameWon{
        fun onReplay()
    }
}