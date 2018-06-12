package com.android.dev.yashchuk.sickleaves.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.android.dev.yashchuk.sickleaves.data.SickLeave

@Dao interface SickLeavesDao {

    @Query("SELECT * from sick_days") fun getSickLeaves(): List<SickLeave>
}