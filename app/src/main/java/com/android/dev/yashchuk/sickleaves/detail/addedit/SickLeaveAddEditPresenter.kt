package com.android.dev.yashchuk.sickleaves.detail.addedit

import com.android.dev.yashchuk.sickleaves.data.SickLeave


class SickLeaveAddEditPresenter(private val view: SickLeaveAddEditContract.View)
    : SickLeaveAddEditContract.Presenter {

    override fun updateUi(sickLeave: SickLeave?) {
        if (sickLeave != null) view.fillSickLeaveData(sickLeave) else view.showEmptySickLeave()
    }

    override fun showDatePicker(requestCode: Int) {
        view.showDatePicker(requestCode)
    }

    override fun saveSickLeave(sickLeave: SickLeave) {
        view.saveSickLeave(sickLeave)
    }

    override fun showLoading(show: Boolean) {
        view.showLoading(show)
    }

    override fun closeScreen() {
        view.closeScreen()
    }
}