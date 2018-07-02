package com.android.dev.yashchuk.sickleaves.sickleaves

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesRepository

class SickLeavesViewModel(private val userId: String,
                          private val sickLeavesRepository: SickLeavesDataSource)
    : ViewModel() {

    fun start() {
        loadSickLeaves(false, true)
    }

    private fun loadSickLeaves(forceUpdate: Boolean, showLoading: Boolean) {
        if (forceUpdate) {
            sickLeavesRepository.refreshSickLeaves()
        }

        sickLeavesRepository.getSickLeaves(userId, object : SickLeavesDataSource.LoadSickLeavesCallback {
            override fun onSickLeavesLoaded(sickLeaves: List<SickLeave>) {

            }

            override fun onDataNotAvailable() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}