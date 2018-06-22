package com.android.dev.yashchuk.sickleaves.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.getFormattedDate(): String? {
    return try {
        val parsedDate = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).parse(this)
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(parsedDate)
    } catch (e: ParseException) {
        null
    }
}
