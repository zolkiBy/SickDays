package com.android.dev.yashchuk.sickleaves.sickleaves

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status

class SickLeavesPresenter(private val view: SickLeavesContract.View): SickLeavesContract.Presenter {

    override fun showLoading(show: Boolean) {
        view.showLoading(show)
    }

    override fun updateUi(sickLeaves: List<SickLeave>?) {
        view.updateUi(sickLeaves)
    }

    override fun closeSickLeave(sickLeave: SickLeave) {
        sickLeave.apply {
            status = Status.CLOSE.name
        }

        view.closeSickLeave(sickLeave)
    }

    override fun deleteSickLeave(sickLeave: SickLeave) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}