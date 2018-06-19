package com.android.dev.yashchuk.sickleaves.data.source.local

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.utils.AppExecutors

class SickLeavesLocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val sickLeavesDao: SickLeavesDao
) : SickLeavesDataSource {

    override fun getSickLeaves(callback: SickLeavesDataSource.LoadSickLeavesCallback) {
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

    override fun saveSickLeave(sickLeave: SickLeave, callback: SickLeavesDataSource.SaveSickLeaveCallback) {
        appExecutors.diskIO.execute { sickLeavesDao.insertSickLeave(sickLeave) }
    }

    override fun deleteSickLeave(id: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllSickLeaves(callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}