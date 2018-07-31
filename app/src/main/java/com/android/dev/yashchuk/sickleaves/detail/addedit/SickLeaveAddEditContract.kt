package com.android.dev.yashchuk.sickleaves.detail.addedit

import android.support.annotation.StringRes
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import java.util.*


interface SickLeaveAddEditContract {
    interface View {
        fun fillSickLeaveData(sickLeave: SickLeave)
        fun save(sickLeave: SickLeave)
        fun createSickLeave()
        fun updateSickLeave()
        fun showEmptySickLeave()
        fun showDatePicker(requestCode: Int)
        fun showLoading(show: Boolean)
        fun closeScreen()
        fun setToolbarTextForSickLeave(text: String)
        fun setToolbarTextForNewSickLeave(textResId: Int)
        fun showErrorWithSnackBar(@StringRes messageResId: Int)
    }

    interface Presenter {
        fun updateUi(sickLeave: SickLeave?)
        fun validate(sickLeave: SickLeave?)
        fun save(sickLeave: SickLeave)
        fun close(sickLeave: SickLeave?, endDate: Date?)
        fun showDatePicker(requestCode: Int)
        fun showLoading(show: Boolean)
        fun closeScreen()
        fun setToolbarTitle(sickLeave: SickLeave?)
    }
}