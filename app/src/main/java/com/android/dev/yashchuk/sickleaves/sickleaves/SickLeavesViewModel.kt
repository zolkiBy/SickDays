package com.android.dev.yashchuk.sickleaves.sickleaves

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.FilterType
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.utils.Event

class SickLeavesViewModel(private val userId: String?,
                          private val sickLeavesRepository: SickLeavesDataSource)
    : ViewModel() {

    private val _sickLeaves = MutableLiveData<List<SickLeave>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isLoadingFromSwipe = MutableLiveData<Boolean>()
    private val _snackBarMessage = MutableLiveData<Event<Int>>()
    private val _toolbarTitleResId = MutableLiveData<Int>()

    val sickLeaves: LiveData<List<SickLeave>>
        get() = _sickLeaves
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    val isLoadingFromSwipe: LiveData<Boolean>
        get() = _isLoadingFromSwipe
    val snackBarMessage: LiveData<Event<Int>>
        get() = _snackBarMessage
    val toolbarTitleResId: LiveData<Int>
        get() = _toolbarTitleResId

    var currentFiltering = FilterType.ALL
    set(value) {
        field = value
        updateFiltering()
    }

    init {
        loadSickLeaves(true, false)
    }

    private fun updateFiltering() {
        when (currentFiltering) {
            FilterType.ALL -> _toolbarTitleResId.value = R.string.sick_list_toolbar_title_all
            FilterType.OPEN -> _toolbarTitleResId.value = R.string.sick_list_toolbar_title_opened
            FilterType.CLOSE -> _toolbarTitleResId.value = R.string.sick_list_toolbar_title_closed
        }
    }

    fun loadSickLeaves(forceUpdate: Boolean, isFromSwipe: Boolean) {
        userId?.let {
            if (!isFromSwipe) {
                _isLoading.value = true
            }

            if (forceUpdate) {
                sickLeavesRepository.refreshSickLeaves()
            }

            sickLeavesRepository.getSickLeaves(it, object : SickLeavesDataSource.LoadSickLeavesCallback {
                override fun onSickLeavesLoaded(sickLeaves: List<SickLeave>) {
                    val sickLeavesToShow: List<SickLeave> = when (currentFiltering) {
                        FilterType.ALL -> sickLeaves
                        FilterType.OPEN -> sickLeaves.filter { it.status == Status.OPEN.name }
                        FilterType.CLOSE -> sickLeaves.filter { it.status == Status.CLOSE.name }
                    }

                    _isLoading.value = false
                    _isLoadingFromSwipe.value = false
                    this@SickLeavesViewModel._sickLeaves.value = sickLeavesToShow
                }

                override fun onDataNotAvailable() {
                    _isLoading.value = false
                    _isLoadingFromSwipe.value = false
                    _sickLeaves.value = listOf()
                }
            })
        }
    }

    fun closeSickLeave(sickLeave: SickLeave) {
        userId?.let {
            _isLoading.value = true
            sickLeavesRepository.saveSickLeave(it, sickLeave, object : SickLeavesDataSource.SaveSickLeaveCallback {
                override fun onSickLeaveSaved() {
                    loadSickLeaves(false, false)
                    _isLoading.value = false
                    _snackBarMessage.value = Event(R.string.sick_list_close_success_message)
                }

                override fun onSickLeaveSaveFailed() {
                    _isLoading.value = false
                    _snackBarMessage.value = Event(R.string.sick_list_close_error_message)
                }
            })
        }
    }

    fun deleteSickLeave(sickLeave: SickLeave) {
        userId?.let {
            sickLeavesRepository.deleteSickLeave(
                    it,
                    sickLeave.id.toString(),
                    object : SickLeavesDataSource.DeleteSickLeaveCallback {
                        override fun onSickLeaveDeleted() {
                            loadSickLeaves(false, false)
                            _snackBarMessage.value = Event(R.string.sick_list_delete_success_message)
                        }

                        override fun onSickLeaveDeleteFailed() {
                            loadSickLeaves(false, false)
                            _snackBarMessage.value = Event(R.string.sick_list_delete_error_message)
                        }
                    })
        }
    }
}