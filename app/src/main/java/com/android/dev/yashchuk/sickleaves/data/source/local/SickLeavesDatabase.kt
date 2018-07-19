package com.android.dev.yashchuk.sickleaves.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.android.dev.yashchuk.sickleaves.data.SickLeave

@Database(entities = arrayOf(SickLeave::class), exportSchema = false, version = 1)
@TypeConverters(DateConverter::class)
abstract class SickLeavesDatabase : RoomDatabase() {

    abstract fun sickLeavesDao(): SickLeavesDao

    companion object {

        private const val DATABASE_NAME = "SickLeaves.db"
        private var INSTANCE: SickLeavesDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): SickLeavesDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            SickLeavesDatabase::class.java, DATABASE_NAME)
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}