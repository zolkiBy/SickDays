package com.android.dev.yashchuk.sickleaves.utils

import android.content.Context
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesRepository
import com.android.dev.yashchuk.sickleaves.data.source.local.SickLeavesDatabase
import com.android.dev.yashchuk.sickleaves.data.source.local.SickLeavesLocalDataSource
import com.android.dev.yashchuk.sickleaves.data.source.remote.SickLeavesRemoteDataSource
import com.android.dev.yashchuk.sickleaves.sickleaves.SickLeavesViewModelFactory

object Injection {

    fun provideSickLeaveRepository(context: Context) =
            SickLeavesRepository(SickLeavesLocalDataSource.getInstance(AppExecutors(),
                    SickLeavesDatabase.getInstance(context.applicationContext).sickLeavesDao()),
                    SickLeavesRemoteDataSource.getInstance())


    fun provideSickLeavesViewModelFactory(context: Context, userId: String) =
            SickLeavesViewModelFactory(provideSickLeaveRepository(context.applicationContext), userId)
}