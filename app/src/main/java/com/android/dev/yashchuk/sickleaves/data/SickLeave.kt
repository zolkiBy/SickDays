package com.android.dev.yashchuk.sickleaves.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import java.util.*

@Entity(tableName = "sick_leaves")
data class SickLeave(
        @ColumnInfo(name = "entryid") @PrimaryKey var id: Long,
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "description") var description: String = "",
        @ColumnInfo(name = "start_date") var startDate: Date? = Date(),
        @ColumnInfo(name = "end_date") var endDate: Date? = null,
        @ColumnInfo(name = "status") var status: String = Status.OPEN.name
){
    @Ignore
    constructor() : this(0)
}