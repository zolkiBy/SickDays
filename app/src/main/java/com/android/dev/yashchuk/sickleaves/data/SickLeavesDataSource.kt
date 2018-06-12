package com.android.dev.yashchuk.sickleaves.data

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
        fun onSickLeaveSuccesfullySaved()
        fun onSickDaySaveFailed()
    }

    interface DeleteSickLeaveCallback {
        fun onSickLeaveSuccesfullyDeleted()
        fun onSickDayDeleteFailed()
    }

    interface DeleteAllSickLeavesCallback {
        fun onSickLeavesSuccesfullyDeleted()
        fun onSickDaysDeleteFailed()
    }

    fun getSickLeaves(callback: LoadSickLeavesCallback)

    fun getSickLeave(sickDayId: String, callback: GetSickLeaveCallback)

    fun saveSickLeave(sickLeave: SickLeave, callback: SaveSickLeaveCallback)

    fun deleteSickLeave(sickDayId: String, callback: DeleteSickLeaveCallback)

    fun deleteAllSickLeaves(callback: DeleteAllSickLeavesCallback)
}