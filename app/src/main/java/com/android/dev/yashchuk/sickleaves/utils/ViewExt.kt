package com.android.dev.yashchuk.sickleaves.utils

import android.support.design.widget.Snackbar
import android.view.View

fun View.showSnackBar(text: String, length: Int) {
    Snackbar.make(this, text, length).show()
}