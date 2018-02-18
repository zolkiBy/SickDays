package com.android.dev.yashchuk.sickdays.data

import java.util.*

data class SickDay @JvmOverloads constructor(
        var title: String = "",
        var description: String = "",
        var id: String = UUID.randomUUID().toString()
) {
}