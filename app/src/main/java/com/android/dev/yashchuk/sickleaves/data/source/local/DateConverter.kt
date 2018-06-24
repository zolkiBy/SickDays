package com.android.dev.yashchuk.sickleaves.data.source.local

import android.arch.persistence.room.TypeConverter
import java.util.*

object DateConverter {
    @TypeConverter
    fun fromTimestamp(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) null else date.time
    }
}