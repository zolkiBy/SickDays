package com.android.dev.yashchuk.sickleaves.sickleaves

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status

class SickLeavesPresenter(private val view: SickLeavesContract.View): SickLeavesContract.Presenter {

    override fun showLoading(show: Boolean) {
        if (show) view.hideEmptyView()
        view.showLoading(show)
    }

    override fun updateUi(sickLeaves: List<SickLeave>?) {
        if (sickLeaves != null && sickLeaves.isNotEmpty()) {
            view.hideEmptyView()
            view.showDataList()
            view.updateUi(sickLeaves)
        } else {
            view.hideDataList()
            view.showEmptyView()
        }
    }

    override fun closeSickLeave(sickLeave: SickLeave) {
        sickLeave.apply {
            status = Status.CLOSE.name
        }

        view.closeSickLeave(sickLeave)
    }

    override fun deleteSickLeave(sickLeave: SickLeave) {
        view.deleteSickLeave(sickLeave)
    }

    override fun showAll() {
        view.showAll()
    }

    override fun showOpened() {
        view.showOpened()
    }

    override fun showClosed() {
        view.showClosed()
    }
}