package com.android.dev.yashchuk.sickleaves.data.source.local

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource

class SickLeavesLocalDataSource: SickLeavesDataSource {
    override fun getSickLeaves(callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSickLeave(id: String, callback: SickLeavesDataSource.GetSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveSickLeave(sickLeave: SickLeave, callback: SickLeavesDataSource.SaveSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSickLeave(id: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllSickLeaves(callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}