package com.android.dev.yashchuk.sickleaves.data.source.local

import android.support.annotation.VisibleForTesting
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.utils.AppExecutors

class SickLeavesLocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val sickLeavesDao: SickLeavesDao
) : SickLeavesDataSource {

    override fun getSickLeaves(userId: String, callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        appExecutors.diskIO.execute {
            val sickLeaves = sickLeavesDao.getSickLeaves()
            appExecutors.mainThread.execute {
                if (sickLeaves.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onSickLeavesLoaded(sickLeaves)
                }
            }
        }
    }

    override fun getSickLeave(id: String, callback: SickLeavesDataSource.GetSickLeaveCallback) {
        appExecutors.diskIO.execute {
            val sickLeave = sickLeavesDao.getSickLeave(id)
            appExecutors.mainThread.execute {
                if (sickLeave == null) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onSickLeaveLoaded(sickLeave)
                }
            }
        }
    }

    override fun saveSickLeave(userId: String, sickLeave: SickLeave, callback: SickLeavesDataSource.SaveSickLeaveCallback) {
        appExecutors.diskIO.execute { sickLeavesDao.insertSickLeave(sickLeave) }
    }

    override fun deleteSickLeave(id: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        appExecutors.diskIO.execute { sickLeavesDao.deleteSickLeaveById(id) }
    }

    override fun deleteAllSickLeaves(callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        appExecutors.diskIO.execute { sickLeavesDao.deleteSickLeaves() }
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