package com.android.dev.yashchuk.sickleaves.data.source

import com.android.dev.yashchuk.sickleaves.data.SickLeave

interface SickLeavesDataSource {

    interface LoadSickLeavesCallback {
        fun onSickLeavesLoaded(sickLeaves: List<SickLeave>)
        fun onDataNotAvailable()
    }

    interface GetSickLeaveCallback {
        fun onSickLeaveLoaded(sickLeave: SickLeave)
        fun onDataNotAvailable()
    }

    interface SaveSickLeaveCallback {
        fun onSickLeaveSaved()
        fun onSickLeaveSaveFailed()
    }

    interface DeleteSickLeaveCallback {
        fun onSickLeaveDeleted()
        fun onSickLeaveDeleteFailed()
    }

    interface DeleteAllSickLeavesCallback {
        fun onSickLeavesDeleted()
        fun onSickLeavesDeleteFailed()
    }

    fun getSickLeaves(userId: String, callback: LoadSickLeavesCallback)

    fun getSickLeave(userId: String, sickLeaveId: String, callback: GetSickLeaveCallback)

    fun saveSickLeave(userId: String, sickLeave: SickLeave, callback: SaveSickLeaveCallback)

    fun deleteSickLeave(userId: String, sickLeaveId: String, callback: DeleteSickLeaveCallback)

    fun deleteAllSickLeaves(userId: String, sickLeaves: List<SickLeave>, callback: DeleteAllSickLeavesCallback)

    fun refreshSickLeaves()
}