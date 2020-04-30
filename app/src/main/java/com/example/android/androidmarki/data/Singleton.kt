package com.example.android.androidmarki.data

open class Singleton<out T: Any, in A >(creator: (arg:A) -> T) {
    private var creator: ((arg: A) -> T)? = creator
    @Volatile
    private lateinit var instance: T
    fun get(arg: A): T {
        return synchronized(this) {
            if (!::instance.isInitialized) {
                val created = creator!!(arg)
                instance = created
            }
            instance
        }
    }
    //        companion object : Singleton<Manager, Context>(::Manager)  Manager.get(context)

}