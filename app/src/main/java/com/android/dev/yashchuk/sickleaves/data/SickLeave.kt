package com.android.dev.yashchuk.sickleaves.data

import android.arch.persistence.room.*
import com.android.dev.yashchuk.sickleaves.data.source.local.DateConverter
import java.lang.reflect.Constructor
import java.util.*

@Entity(tableName = "sick_leaves")
@TypeConverters(DateConverter::class)
data class SickLeave (
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "description") var description: String = "",
        @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString(),
         @ColumnInfo(name = "start_date") var startDate: Date = Date(),
         @ColumnInfo(name = "end_date") var endDate: Date? = null,
         @ColumnInfo(name = "status") var status: String = Status.OPEN.name
)