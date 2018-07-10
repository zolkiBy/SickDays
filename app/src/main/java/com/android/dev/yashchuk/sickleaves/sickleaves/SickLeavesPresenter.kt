package com.android.dev.yashchuk.sickleaves.sickleaves

class SickLeavesPresenter(private val view: SickLeavesContract.View): SickLeavesContract.Presenter {

    override fun showLoading(show: Boolean) {
        view.showLoading(show)
    }
}