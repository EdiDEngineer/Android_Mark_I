package com.example.android.androidmarki

import android.app.Application
import androidx.multidex.BuildConfig.DEBUG
import androidx.multidex.MultiDexApplication
import timber.log.Timber

class AndroidMarkI : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

//        Stetho.initializeWithDefaults(this)
        if (DEBUG) Timber.plant(Timber.DebugTree())
        sInstance =this
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        private lateinit var sInstance: AndroidMarkI
        fun get(): AndroidMarkI {
            return sInstance
        }
    }
}