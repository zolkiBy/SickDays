package com.android.dev.yashchuk.sickleaves.utils

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
