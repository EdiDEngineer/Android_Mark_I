package com.example.android.androidmarki

import androidx.annotation.MainThread
import androidx.multidex.BuildConfig.DEBUG
import androidx.multidex.MultiDexApplication
import timber.log.Timber

class AndroidMarkI : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        //for mvvm
//        startKoin(
//            androidContext = this, modules = listOf(
//                appMvvMModule,
//                roomModule,
//                networkModule,
//                providerModule
//            )
//        )
//        Stetho.initializeWithDefaults(this)
        if (DEBUG) Timber.plant(Timber.DebugTree())

        Timber.plant(Timber.DebugTree())
    }

    companion object {
        private lateinit var sInstance: AndroidMarkI

        @MainThread
        fun get(): AndroidMarkI {
            sInstance = if (::sInstance.isInitialized) sInstance else AndroidMarkI()
            return sInstance
        }
    }
}