package com.android.dev.yashchuk.sickleaves.detail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesRepository


@Suppress("UNCHECKED_CAST")
class SickLeaveDetailViewModelFactory(private val userId: String?,
                                      private val repository: SickLeavesRepository )
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SickLeaveDetailViewModel(userId, repository) as T
    }
}