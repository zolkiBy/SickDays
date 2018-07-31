package com.android.dev.yashchuk.sickleaves.detail

import com.android.dev.yashchuk.sickleaves.R
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

    @Test
    fun showDatePicker() {
        addEditPresenter.showDatePicker(0)

        verify(view).showDatePicker(0)
    }

    @Test
    fun validateSickLeave_nonNull_shouldUpdateSickLeave() {
        addEditPresenter.validate(sickLeave)

        verify(view).updateSickLeave()
        verify(view, never()).createSickLeave()
    }

    @Test
    fun validateSickLeave_null_shouldCreateSickLeave() {
        addEditPresenter.validate(null)

        verify(view).createSickLeave()
        verify(view, never()).updateSickLeave()
    }

    @Test
    fun saveSickLeave() {
        addEditPresenter.save(sickLeave)

        verify(view).save(sickLeave)
    }

    @Test
    fun closeSickLeave_nonNull_shouldClose() {
        addEditPresenter.close(sickLeave, null)

        verify(view).save(sickLeave)
    }

    @Test
    fun closeSickLeave_null_shouldNeverClose() {
        addEditPresenter.close(null, null)

        verify(view, never()).save(sickLeave)
    }

    @Test
    fun showLoading() {
        addEditPresenter.showLoading(true)

        verify(view).showLoading(true)
    }

    @Test
    fun closeScreen_shouldCloseScreen() {
        addEditPresenter.closeScreen()

        verify(view).closeScreen()
    }

    @Test
    fun setToolbarTitle_nonNull_shouldSetTitleForCurrentSickLeave() {
        addEditPresenter.setToolbarTitle(sickLeave)

        verify(view).setToolbarTextForSickLeave(sickLeave.title)
        verify(view, never()).setToolbarTextForNewSickLeave(R.string.fragment_add_edit_toolbar_title_create)
    }

    @Test
    fun setToolbarTitle_nonNull_shouldSetTitleForNewSickLeave() {
        addEditPresenter.setToolbarTitle(null)

        verify(view).setToolbarTextForNewSickLeave(R.string.fragment_add_edit_toolbar_title_create)
        verify(view, never()).setToolbarTextForSickLeave(sickLeave.title)
    }
}