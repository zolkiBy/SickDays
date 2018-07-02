package com.android.dev.yashchuk.sickleaves.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesRepository

class SickLeaveDetailViewModel(private val userId: String?,
                               private val repository: SickLeavesRepository)
    : ViewModel() {

    var sickLeave: SickLeave? = null
    var isLoading = MutableLiveData<Boolean>()

    fun start() {
        userId?.let { id ->
            isLoading.value = true
            repository.getSickLeave(id, object : SickLeavesDataSource.GetSickLeaveCallback {
                override fun onSickLeaveLoaded(sickLeave: SickLeave) {
                    isLoading.value = false
                    this@SickLeaveDetailViewModel.sickLeave = sickLeave
                }

                override fun onDataNotAvailable() {
                    isLoading.value = false
                    sickLeave = null
                }
            })
        }
    }
}