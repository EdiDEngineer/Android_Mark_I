package com.example.android.androidmarki

import android.os.Build
import androidx.multidex.MultiDexApplication
import androidx.work.*
import com.example.android.androidmarki.BuildConfig.DEBUG
import com.example.android.androidmarki.data.source.TasksRepository
import com.example.android.androidmarki.di.ServiceLocator
import com.example.android.androidmarki.worker.RefreshDataWorker
import com.facebook.stetho.Stetho
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Override application to setup background work via WorkManager
 */
/**
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 *
 * Also, sets up Timber in the DEBUG BuildConfig. Read Timber's documentation for production setups.
 */
class AndroidMarkI : MultiDexApplication() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    val taskRepository: TasksRepository
        get() = ServiceLocator.provideTasksRepository(this)
    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }


    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        sInstance = this
        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        delayedInit()
    }

    companion object {
        private lateinit var sInstance: AndroidMarkI
        fun getApp(): AndroidMarkI {
            return sInstance
        }
    }
}