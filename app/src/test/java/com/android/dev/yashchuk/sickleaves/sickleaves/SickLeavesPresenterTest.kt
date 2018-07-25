package com.android.dev.yashchuk.sickleaves.sickleaves

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status
import com.android.dev.yashchuk.sickleaves.utils.Injection
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*


class SickLeavesPresenterTest {

    @Mock
    private lateinit var sickLeavesView: SickLeavesContract.View

    private lateinit var sickLeavesPresenter: SickLeavesPresenter

    private lateinit var sickLeaves: MutableList<SickLeave>

    private lateinit var sickLeave: SickLeave

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        sickLeavesPresenter = Injection.provideSickLeavesPresenter(sickLeavesView)

        sickLeaves = mutableListOf(
                SickLeave(
                        id = Date().time,
                        title = "Test1",
                        description = "Description1"
                ),
                SickLeave(
                        id = Date().time,
                        title = "Test2",
                        description = "Description2"
                ),
                SickLeave(
                        id = Date().time,
                        title = "Test3",
                        description = "Description3"
                ),
                SickLeave(
                        id = Date().time,
                        title = "Test4",
                        description = "Description4",
                        status = Status.CLOSE.name)
        )

        sickLeave = SickLeave(
                id = Date().time,
                title = "Test5",
                description = "Description5"
        )
    }

    @Test
    fun showLoading_true_shouldHideEmptyView_shouldShowLoading() {
        sickLeavesPresenter.showLoading(true)

        verify(sickLeavesView).hideEmptyView()
        verify(sickLeavesView).showLoading(true)
    }

    @Test
    fun showLoading_false_shouldOnlyHideLoading() {
        sickLeavesPresenter.showLoading(false)

        verify(sickLeavesView, never()).hideEmptyView()
        verify(sickLeavesView).showLoading(false)
    }

    @Test
    fun updateUi_null_shouldHideData_shouldShowEmptyView() {
        sickLeavesPresenter.updateUi(null)

        verify(sickLeavesView).hideData()
        verify(sickLeavesView).showEmptyView()
    }

    @Test
    fun updateUi_emptyList_shouldHideData_shouldShowEmptyView() {
        sickLeavesPresenter.updateUi(listOf())

        verify(sickLeavesView).hideData()
        verify(sickLeavesView).showEmptyView()
    }

    @Test
    fun updateUi_list_shouldShowData_shouldHideEmptyView() {
        sickLeavesPresenter.updateUi(sickLeaves)

        verify(sickLeavesView).hideEmptyView()
        verify(sickLeavesView).showData()
        verify(sickLeavesView).updateUi(sickLeaves)
    }

    @Test
    fun closeSickLeave_shouldCloseSickLeave() {
        sickLeavesPresenter.closeSickLeave(sickLeave)

        verify(sickLeavesView).closeSickLeave(sickLeave)
    }

    @Test
    fun deleteSickLeave_shouldDeleteSickLeave() {
        sickLeavesPresenter.deleteSickLeave(sickLeave)

        verify(sickLeavesView).deleteSickLeave(sickLeave)
    }

    @Test
    fun showAll_shouldShowAll() {
        sickLeavesPresenter.showAll()

        verify(sickLeavesView).showAll()
    }

    @Test
    fun showOpened_shouldShowOpened() {
        sickLeavesPresenter.showOpened()

        verify(sickLeavesView).showOpened()
    }

    @Test
    fun showClosed_shouldShowClosed() {
        sickLeavesPresenter.showClosed()

        verify(sickLeavesView).showClosed()
    }
}
