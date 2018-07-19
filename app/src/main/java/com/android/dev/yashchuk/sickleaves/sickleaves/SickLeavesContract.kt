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
        fun closeSickLeave(sickLeave: SickLeave)
        fun deleteSickLeave(sickLeave: SickLeave)
        fun showAll()
        fun showOpened()
        fun showClosed()
    }

    interface Presenter {
        fun showLoading(show: Boolean)
        fun updateUi(sickLeaves: List<SickLeave>?)
        fun closeSickLeave(sickLeave: SickLeave)
        fun deleteSickLeave(sickLeave: SickLeave)
        fun showAll()
        fun showOpened()
        fun showClosed()
    }
}