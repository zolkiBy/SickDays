package com.android.dev.yashchuk.sickleaves.sickleaves

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.android.dev.yashchuk.sickleaves.capture
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesRepository
import com.android.dev.yashchuk.sickleaves.eq
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.*


private const val TEST_USER_ID = "nskncsd-324mcmdks-cdsccslc"

class SickLeavesViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: SickLeavesRepository
    @Mock
    private lateinit var observer: Observer<Boolean>
    @Mock
    private lateinit var viewModelCallback: SickLeavesDataSource.LoadSickLeavesCallback
    @Captor
    private lateinit var loadSickLeavesCallbackCaptor: ArgumentCaptor<SickLeavesDataSource.LoadSickLeavesCallback>

    private lateinit var sickLeaves: MutableList<SickLeave>

    private lateinit var viewModel: SickLeavesViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        viewModel = SickLeavesViewModel(TEST_USER_ID, repository)

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
    }

    @Test
    fun loadSickLeaves_dataLoaded() {
        viewModelCallback = mock(SickLeavesDataSource.LoadSickLeavesCallback::class.java)
        with(viewModel) {
            isLoading.observeForever(observer)
            assertTrue(isLoading.value!!)

            verify<SickLeavesRepository>(repository).getSickLeaves(eq(TEST_USER_ID), capture(loadSickLeavesCallbackCaptor))
            loadSickLeavesCallbackCaptor.value.onSickLeavesLoaded(this@SickLeavesViewModelTest.sickLeaves)

            assertFalse(isLoading.value!!)

            assertFalse(sickLeaves.value!!.isEmpty())
            assertTrue(sickLeaves.value!!.size == 4)
        }
    }

    @Test
    fun loadSickLeaves_dataNotAvailable() {

    }
}