package com.android.dev.yashchuk.sickleaves.detail

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.detail.addedit.SickLeaveAddEditContract
import com.android.dev.yashchuk.sickleaves.detail.addedit.SickLeaveAddEditPresenter
import com.android.dev.yashchuk.sickleaves.utils.Injection
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

class SickLeaveAddEditPresenterTest {

    @Mock
    lateinit var view: SickLeaveAddEditContract.View

    private lateinit var addEditPresenter: SickLeaveAddEditPresenter
    private lateinit var sickLeave: SickLeave

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        addEditPresenter = Injection.provideSickLeaveDetailPresenter(view)

        sickLeave = SickLeave(
                id = Date().time,
                title = "AddEditTest",
                description = "AddEditDescription"
        )
    }

    @Test
    fun updateUi_nonNull_shouldFillSickLeaveData() {
        addEditPresenter.updateUi(sickLeave)

        verify(view).fillSickLeaveData(sickLeave)
        verify(view, never()).showEmptySickLeave()
    }

    @Test
    fun updateUi_null_shouldShowEmptySickLeave() {
        addEditPresenter.updateUi(null)

        verify(view).showEmptySickLeave()
        verify(view, never()).fillSickLeaveData(sickLeave)
    }
}