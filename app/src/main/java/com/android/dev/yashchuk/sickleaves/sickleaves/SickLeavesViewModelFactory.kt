package com.android.dev.yashchuk.sickleaves.sickleaves

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource


@Suppress("UNCHECKED_CAST")
class SickLeavesViewModelFactory(private val userId: String,
                                 private val sickLeavesRepository: SickLeavesDataSource)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SickLeavesViewModel(userId, sickLeavesRepository) as T
    }
}