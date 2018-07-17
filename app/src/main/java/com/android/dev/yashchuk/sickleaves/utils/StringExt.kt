package com.android.dev.yashchuk.sickleaves.utils

import android.support.annotation.StringRes
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.Status
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.getFormattedDate(): Date? {
    return try {
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(this)
    } catch (e: ParseException) {
        null
    }
}
@StringRes
fun String.getStatusStringRes(): Int {
    return when (this) {
        Status.OPEN.name -> R.string.sick_list_status_open
        Status.CLOSE.name -> R.string.sick_list_status_closed
        else -> throw IllegalArgumentException("Unknown status")
    }
}
