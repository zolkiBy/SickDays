package com.android.dev.yashchuk.sickleaves.sickleaves

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesRepository

class SickLeavesViewModel(private val userId: String,
                          private val sickLeavesRepository: SickLeavesDataSource)
    : ViewModel() {

    val sickLeaves = MutableLiveData<List<SickLeave>>()
    val isLoading = MutableLiveData<Boolean>()

    fun start() {
        loadSickLeaves(false, true)
    }

    private fun loadSickLeaves(forceUpdate: Boolean, showLoading: Boolean) {
        isLoading.value = true

        if (forceUpdate) {
            sickLeavesRepository.refreshSickLeaves()
        }

        sickLeavesRepository.getSickLeaves(userId, object : SickLeavesDataSource.LoadSickLeavesCallback {
            override fun onSickLeavesLoaded(sickLeaves: List<SickLeave>) {
                isLoading.value = false
                this@SickLeavesViewModel.sickLeaves.value = sickLeaves
            }

            override fun onDataNotAvailable() {
                isLoading.value = false
                sickLeaves.value = null
            }
        })
    }
}