package com.android.dev.yashchuk.sickleaves.detail

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesRepository


class SickLeaveDetailPresenter(private val view: SickLeaveDetailContract.View,
                               private val repository: SickLeavesRepository)
    : SickLeaveDetailContract.Presenter {

    override fun updateUi(sickLeave: SickLeave?) {
        if (sickLeave != null) view.fillSickLeaveData(sickLeave) else view.showEmptySickLeave()
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