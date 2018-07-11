package com.android.dev.yashchuk.sickleaves.sickleaves

import com.android.dev.yashchuk.sickleaves.data.SickLeave

class SickLeavesPresenter(private val view: SickLeavesContract.View): SickLeavesContract.Presenter {

    override fun showLoading(show: Boolean) {
        view.showLoading(show)
    }

    override fun updateUi(sickLeaves: List<SickLeave>?) {
        view.updateUi(sickLeaves)
    }

    override fun closeSickLeave() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}