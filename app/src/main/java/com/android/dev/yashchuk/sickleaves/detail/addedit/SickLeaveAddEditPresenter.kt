package com.android.dev.yashchuk.sickleaves.detail.addedit

import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status


class SickLeaveAddEditPresenter(private val view: SickLeaveAddEditContract.View)
    : SickLeaveAddEditContract.Presenter {

    override fun updateUi(sickLeave: SickLeave?) {
        if (sickLeave != null) view.fillSickLeaveData(sickLeave) else view.showEmptySickLeave()
    }

    override fun showDatePicker(requestCode: Int) {
        view.showDatePicker(requestCode)
    }

    override fun validate(sickLeave: SickLeave?) {
        if (sickLeave == null) view.createSickLeave() else view.updateSickLeave()
    }

    override fun save(sickLeave: SickLeave) {
        view.save(sickLeave)
    }

    override fun close(sickLeave: SickLeave?) {
        if (sickLeave != null) {
            sickLeave.status = Status.CLOSE.name
            view.save(sickLeave)
        }
    }

    override fun showLoading(show: Boolean) {
        view.showLoading(show)
    }

    override fun closeScreen() {
        view.closeScreen()
    }

    override fun setToolbarTitle(sickLeave: SickLeave?) {
        if (sickLeave == null) {
            view.setToolbarTextForNewSickLeave(R.string.fragment_add_edit_toolbar_title_create)
        } else {
            view.setToolbarTextForSickLeave(sickLeave.title)
        }
    }
}