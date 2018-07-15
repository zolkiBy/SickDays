package com.android.dev.yashchuk.sickleaves.data.source.local

import android.arch.persistence.room.*
import com.android.dev.yashchuk.sickleaves.data.SickLeave

@Dao
interface SickLeavesDao {

    @Query("SELECT * FROM sick_leaves")
    fun getSickLeaves(): List<SickLeave>

    @Query("SELECT * FROM sick_leaves WHERE entryid = :id")
    fun getSickLeave(id: String): SickLeave?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSickLeave(sickLeave: SickLeave)

    @Update
    fun updateSickLeave(sickLeave: SickLeave): Int

    @Query("DELETE FROM sick_leaves")
    fun deleteSickLeaves()

    @Query("DELETE FROM sick_leaves WHERE entryid = :id")
    fun deleteSickLeaveById(id: String)
}