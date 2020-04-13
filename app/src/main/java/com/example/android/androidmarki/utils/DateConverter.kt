package com.example.android.androidmarki.utils

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    @TypeConverter
    @JvmStatic
    fun toDate(timestamp: Long?): Date? = timestamp?.let { Date(timestamp) }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(date: Date?): Long? = date?.time
}