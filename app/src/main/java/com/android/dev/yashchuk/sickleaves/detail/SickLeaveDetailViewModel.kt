package com.android.dev.yashchuk.sickleaves.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesRepository
import com.android.dev.yashchuk.sickleaves.utils.Event

class SickLeaveDetailViewModel(private val userId: String?,
                               private val repository: SickLeavesRepository)
    : ViewModel() {

    private val _snackBarMessage = MutableLiveData<Event<Int>>()
    private val _sickLeave = MutableLiveData<SickLeave>()
    private val _isLoading = MutableLiveData<Boolean>()

    val snackBarMessage: LiveData<Event<Int>>
        get() = _snackBarMessage
    val sickLeave: LiveData<SickLeave>
        get() = _sickLeave
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun loadSickLeave(sickLeaveId: String) {
        userId?.let { _ ->
            _isLoading.value = true
            repository.getSickLeave(sickLeaveId, object : SickLeavesDataSource.GetSickLeaveCallback {
                override fun onSickLeaveLoaded(sickLeave: SickLeave) {
                    _isLoading.value = false
                    this@SickLeaveDetailViewModel._sickLeave.value = sickLeave
                }

                override fun onDataNotAvailable() {
                    _isLoading.value = false
                    _sickLeave.value = null
                    _snackBarMessage.value = Event(R.string.fragment_detail_failed_load_sick_leave)
                }
            })
        }
    }

    fun saveSickLeave(userId: String, sickLeave: SickLeave) {
        repository.saveSickLeave(userId, sickLeave, object : SickLeavesDataSource.SaveSickLeaveCallback {
            override fun onSickLeaveSaved() {
                this@SickLeaveDetailViewModel._sickLeave.value = sickLeave
            }

            override fun onSickLeaveSaveFailed() {
                _snackBarMessage.value = Event(R.string.fragment_detail_failed_save_sick_leave)
            }
        })
    }
}