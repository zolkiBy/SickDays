package com.android.dev.yashchuk.sickleaves.data.source.local

import android.database.sqlite.SQLiteException
import android.support.annotation.VisibleForTesting
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.utils.AppExecutors

class SickLeavesLocalDataSource private constructor(
        private val appExecutors: AppExecutors,
        private val sickLeavesDao: SickLeavesDao
) : SickLeavesDataSource {

    override fun getSickLeaves(userId: String, callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        appExecutors.diskIO.execute {
            try {
                val sickLeaves = sickLeavesDao.getSickLeaves()
                appExecutors.mainThread.execute {
                    if (sickLeaves.isEmpty()) {
                        callback.onDataNotAvailable()
                    } else {
                        callback.onSickLeavesLoaded(sickLeaves)
                    }
                }
            } catch (exception: SQLiteException) {
                appExecutors.mainThread.execute { callback.onDataNotAvailable() }
            }
        }
    }

    override fun getSickLeave(userId: String, sickLeaveId: String, callback: SickLeavesDataSource.GetSickLeaveCallback) {
        appExecutors.diskIO.execute {
            try {
                val sickLeave = sickLeavesDao.getSickLeave(sickLeaveId)
                appExecutors.mainThread.execute {
                    if (sickLeave == null) {
                        callback.onDataNotAvailable()
                    } else {
                        callback.onSickLeaveLoaded(sickLeave)
                    }
                }
            } catch (exception: SQLiteException) {
                appExecutors.mainThread.execute { callback.onDataNotAvailable() }
            }
        }
    }

    override fun saveSickLeave(userId: String, sickLeave: SickLeave, callback: SickLeavesDataSource.SaveSickLeaveCallback) {
        appExecutors.diskIO.execute {
            try {
                sickLeavesDao.insertSickLeave(sickLeave)
                appExecutors.mainThread.execute { callback.onSickLeaveSaved() }
            } catch (exception: SQLiteException) {
                appExecutors.mainThread.execute { callback.onSickLeaveSaveFailed() }
            }
        }
    }

    override fun deleteSickLeave(userId: String, sickLeaveId: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        appExecutors.diskIO.execute {
            try {
                sickLeavesDao.deleteSickLeaveById(sickLeaveId)
                appExecutors.mainThread.execute { callback.onSickLeaveDeleted() }
            } catch (exception: SQLiteException) {
                appExecutors.mainThread.execute { callback.onSickLeaveDeleteFailed() }
            }
        }
    }

    override fun deleteAllSickLeaves(userId: String, sickLeaves: List<SickLeave>, callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        appExecutors.diskIO.execute {
            try {
                sickLeavesDao.deleteSickLeaves()
                appExecutors.mainThread.execute { callback.onSickLeavesDeleted() }
            } catch (exception: SQLiteException) {
                appExecutors.mainThread.execute { callback.onSickLeavesDeleteFailed() }
            }
        }
    }

    override fun refreshSickLeaves() {
        // implemented in repository
    }

    companion object {
        private var INSTANCE: SickLeavesLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors,
                        sickLeavesDao: SickLeavesDao)
                : SickLeavesLocalDataSource {
            INSTANCE ?: synchronized(SickLeavesLocalDataSource::class.java) {
                INSTANCE ?: SickLeavesLocalDataSource(appExecutors, sickLeavesDao)
                        .also { sickLeavesLocalDataSource ->
                            INSTANCE = sickLeavesLocalDataSource
                        }
            }

            return INSTANCE!!
        }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}