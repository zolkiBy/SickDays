package com.android.dev.yashchuk.sickleaves.callbacks

import android.support.annotation.StringRes


interface OnToolbarTitleSetListener {
    fun onToolbarTitleSet(@StringRes titleResId: Int)
}