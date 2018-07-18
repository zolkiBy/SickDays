package com.android.dev.yashchuk.sickleaves.detail.addedit

import com.android.dev.yashchuk.sickleaves.data.SickLeave


interface SickLeaveDetailContract {
    interface View {
        fun fillSickLeaveData(sickLeave: SickLeave)
        fun showEmptySickLeave()
        fun showDatePicker(requestCode: Int)
    }

    interface Presenter {
        fun updateUi(sickLeave: SickLeave?)
        fun saveSickLeave(userId: String, sickLeave: SickLeave)
        fun showDatePicker(requestCode: Int)
    }
}