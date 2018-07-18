package com.android.dev.yashchuk.sickleaves.callbacks

import android.support.annotation.StringRes


interface OnTitleResChangeListener {
    fun onTitleResChange(@StringRes titleResId: Int)
}