package com.android.dev.yashchuk.sickleaves.sickleaves

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource

class SickLeavesViewModel(private val userId: String?,
                          private val sickLeavesRepository: SickLeavesDataSource)
    : ViewModel() {

    private val _sickLeaves = MutableLiveData<List<SickLeave>>()
    private val _isLoading = MutableLiveData<Boolean>()

    val sickLeaves: LiveData<List<SickLeave>>
        get() = _sickLeaves
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        loadSickLeaves(false, true)
    }

    private fun loadSickLeaves(forceUpdate: Boolean, showLoading: Boolean) {
        userId?.let {
            _isLoading.value = true

            if (forceUpdate) {
                sickLeavesRepository.refreshSickLeaves()
            }

            sickLeavesRepository.getSickLeaves(userId, object : SickLeavesDataSource.LoadSickLeavesCallback {
                override fun onSickLeavesLoaded(sickLeaves: List<SickLeave>) {
                    _isLoading.value = false
                    this@SickLeavesViewModel._sickLeaves.value = sickLeaves
                }

                override fun onDataNotAvailable() {
                    _isLoading.value = false
                    _sickLeaves.value = null
                }
            })
        }
    }
}