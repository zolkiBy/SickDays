package com.android.dev.yashchuk.sickleaves.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "sick_leaves")
data class SickLeave @JvmOverloads constructor(
        @PrimaryKey var id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "description") var description: String = ""
       /* @ColumnInfo(name = "start_date") var startDate: Long,
        @ColumnInfo(name = "end_date") var endDate: Long,
        @ColumnInfo(name = "status") var status: String*/
){}