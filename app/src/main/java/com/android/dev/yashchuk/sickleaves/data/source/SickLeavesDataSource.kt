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
        fun onSickLeaveSaveFailed()
    }

    interface DeleteSickLeaveCallback {
        fun onSickLeaveSuccesfullyDeleted()
        fun onSickLeaveDeleteFailed()
    }

    interface DeleteAllSickLeavesCallback {
        fun onSickLeavesDeleteFailed()
    }

    fun getSickLeaves(callback: LoadSickLeavesCallback)

    fun getSickLeave(id: String, callback: GetSickLeaveCallback)

    fun saveSickLeave(sickLeave: SickLeave, callback: SaveSickLeaveCallback)

    fun deleteSickLeave(id: String, callback: DeleteSickLeaveCallback)

    fun deleteAllSickLeaves(callback: DeleteAllSickLeavesCallback)
}