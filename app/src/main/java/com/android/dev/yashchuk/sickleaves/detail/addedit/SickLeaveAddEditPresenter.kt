package com.android.dev.yashchuk.sickleaves.detail.addedit

import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status
import java.util.*


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
        if (sickLeave.endDate != null
                && isEndDateGreaterThenStartDate(sickLeave)) {
            view.save(sickLeave)

        } else if (sickLeave.endDate != null) {
            view.showErrorWithSnackBar(R.string.fragment_add_edit_invalid_date_error_message)
        } else {
            view.save(sickLeave)
        }
    }

    override fun close(sickLeave: SickLeave?) {
        if (sickLeave != null) {
            sickLeave.status = Status.CLOSE.name
            sickLeave.endDate = Date()
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

    private fun isEndDateGreaterThenStartDate(sickLeave: SickLeave): Boolean {
        return sickLeave.endDate!!.time > sickLeave.startDate!!.time
    }
}