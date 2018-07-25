package com.android.dev.yashchuk.sickleaves.sickleaves

import com.android.dev.yashchuk.sickleaves.data.SickLeave

interface SickLeavesContract {
    interface View {
        fun showLoading(show: Boolean)
        fun updateUi(sickLeaves: List<SickLeave>?)
        fun showEmptyView()
        fun hideEmptyView()
        fun showData()
        fun hideData()
        fun closeSickLeave(sickLeave: SickLeave)
        fun deleteSickLeave(sickLeave: SickLeave)
        fun showAll()
        fun showOpened()
        fun showClosed()
        fun logToken(token: String)
    }

    interface Presenter {
        fun showLoading(show: Boolean)
        fun updateUi(sickLeaves: List<SickLeave>?)
        fun closeSickLeave(sickLeave: SickLeave)
        fun deleteSickLeave(sickLeave: SickLeave)
        fun showAll()
        fun showOpened()
        fun showClosed()
        fun logFcmToken()
    }
}