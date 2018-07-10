package com.android.dev.yashchuk.sickleaves.sickleaves

interface SickLeavesContract {
    interface View {
        fun showLoading(show: Boolean)
        fun showEmptyView()
    }

    interface Presenter {
        fun showLoading(show: Boolean)
        fun updateUi()
    }
}