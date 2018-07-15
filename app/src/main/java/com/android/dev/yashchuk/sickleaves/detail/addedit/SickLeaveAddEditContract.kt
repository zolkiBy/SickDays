package com.android.dev.yashchuk.sickleaves.detail.addedit

import com.android.dev.yashchuk.sickleaves.data.SickLeave


interface SickLeaveAddEditContract {
    interface View {
        fun fillSickLeaveData(sickLeave: SickLeave)
        fun saveSickLeave(sickLeave: SickLeave)
        fun showEmptySickLeave()
        fun showDatePicker(requestCode: Int)
        fun showLoading(show: Boolean)
    }

    interface Presenter {
        fun updateUi(sickLeave: SickLeave?)
        fun saveSickLeave(sickLeave: SickLeave)
        fun showDatePicker(requestCode: Int)
        fun showLoading(show: Boolean)
    }
}