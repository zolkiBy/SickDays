package com.android.dev.yashchuk.sickleaves.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.getFormattedDate(): String {
    return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(this)
}
