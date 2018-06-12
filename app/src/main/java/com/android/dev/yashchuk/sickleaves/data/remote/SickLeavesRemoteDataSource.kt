package com.android.dev.yashchuk.sickleaves.data.remote

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.SickLeavesDataSource

class SickLeavesRemoteDataSource: SickLeavesDataSource {
    override fun getSickLeaves(callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSickLeave(sickDayId: String, callback: SickLeavesDataSource.GetSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveSickLeave(sickLeave: SickLeave, callback: SickLeavesDataSource.SaveSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSickLeave(sickDayId: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllSickLeaves(callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
