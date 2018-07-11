package com.android.dev.yashchuk.sickleaves.sickleaves

import com.android.dev.yashchuk.sickleaves.data.SickLeave

interface SickLeavesContract {
    interface View {
        fun showLoading(show: Boolean)
        fun updateUi(sickLeaves: List<SickLeave>?)
        fun showEmptyView()
        fun hideEmptyView()
        fun showDataList()
        fun hideDataList()
        fun closeSickLeave()
    }

    interface Presenter {
        fun showLoading(show: Boolean)
        fun updateUi(sickLeaves: List<SickLeave>?)
        fun closeSickLeave(sickLeave: SickLeave)
    }
}