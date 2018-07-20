package com.android.dev.yashchuk.sickleaves

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.sickleaves.SickLeavesContract
import com.android.dev.yashchuk.sickleaves.sickleaves.SickLeavesPresenter
import com.android.dev.yashchuk.sickleaves.utils.Injection
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.never
import org.mockito.MockitoAnnotations


class SickLeavesPresenterTest {

    @Mock private lateinit var sickLeavesView: SickLeavesContract.View

    private lateinit var sickLeavesPresenter: SickLeavesPresenter

    private lateinit var sickLeaves: MutableList<SickLeave>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)


    }

    @Test
    fun showLoading_true_shouldHideEmptyView_shouldShowLoading() {
        sickLeavesPresenter = Injection.provideSickLeavesPresenter(sickLeavesView)
        sickLeavesPresenter.showLoading(true)

        verify(sickLeavesView).hideEmptyView()
        verify(sickLeavesView).showLoading(true)
    }

    @Test
    fun showLoading_false_shouldOnlyHideLoading() {
        sickLeavesPresenter = Injection.provideSickLeavesPresenter(sickLeavesView)
        sickLeavesPresenter.showLoading(false)

        verify(sickLeavesView, never()).hideEmptyView()
        verify(sickLeavesView).showLoading(false)
    }
}
