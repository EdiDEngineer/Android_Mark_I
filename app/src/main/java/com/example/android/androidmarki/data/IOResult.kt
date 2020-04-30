package com.example.android.androidmarki.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class IOResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : IOResult<T>()
    data class Error(val exception: Exception) : IOResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

}
