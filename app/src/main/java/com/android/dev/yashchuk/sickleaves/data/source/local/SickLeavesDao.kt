package com.android.dev.yashchuk.sickleaves.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.android.dev.yashchuk.sickleaves.data.SickLeave

@Dao interface SickLeavesDao {

    @Query("SELECT * from sick_leaves") fun getSickLeaves(): List<SickLeave>
}