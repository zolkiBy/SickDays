package com.android.dev.yashchuk.sickleaves.detail.addedit

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesRepository


class SickLeaveAddEditPresenter(private val view: SickLeaveAddEditContract.View,
                                private val repository: SickLeavesRepository)
    : SickLeaveAddEditContract.Presenter {

    override fun updateUi(sickLeave: SickLeave?) {
        if (sickLeave != null) view.fillSickLeaveData(sickLeave) else view.showEmptySickLeave()
    }

    override fun showDatePicker(requestCode: Int) {
        view.showDatePicker(requestCode)
    }

    override fun saveSickLeave(userId: String, sickLeave: SickLeave) {
        repository.saveSickLeave(userId, sickLeave,  object : SickLeavesDataSource.SaveSickLeaveCallback {
            override fun onSickLeaveSaved() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSickLeaveSaveFailed() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}