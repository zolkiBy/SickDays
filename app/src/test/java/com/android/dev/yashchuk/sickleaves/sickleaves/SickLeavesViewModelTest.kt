package com.android.dev.yashchuk.sickleaves.sickleaves

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.capture
import com.android.dev.yashchuk.sickleaves.data.FilterType
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesRepository
import com.android.dev.yashchuk.sickleaves.eq
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*


private const val TEST_USER_ID = "nskncsd-324mcmdks-cdsccslc"

class SickLeavesViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: SickLeavesRepository
    @Mock
    private lateinit var viewModelCallback: SickLeavesDataSource.LoadSickLeavesCallback
    @Captor
    private lateinit var loadSickLeavesCallbackCaptor: ArgumentCaptor<SickLeavesDataSource.LoadSickLeavesCallback>
    @Captor
    private lateinit var saveSickLeavesCallbackCaptor: ArgumentCaptor<SickLeavesDataSource.SaveSickLeaveCallback>
    @Captor
    private lateinit var deleteSickLeaveCallbackCaptor: ArgumentCaptor<SickLeavesDataSource.DeleteSickLeaveCallback>
    @Captor
    private lateinit var sickLeaveCaptor: ArgumentCaptor<SickLeave>

    private lateinit var sickLeave: SickLeave
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

        sickLeave = SickLeave(
                id = Date().time,
                title = "Test5",
                description = "Description5"
        )
    }

    @Test
    fun loadSickLeaves_dataLoaded() {
        viewModelCallback = mock(SickLeavesDataSource.LoadSickLeavesCallback::class.java)

        with(viewModel) {
            assertTrue(isLoading.value!!)

            verify<SickLeavesRepository>(repository)
                    .getSickLeaves(eq(TEST_USER_ID), capture(loadSickLeavesCallbackCaptor))
            loadSickLeavesCallbackCaptor.value.onSickLeavesLoaded(this@SickLeavesViewModelTest.sickLeaves)

            assertFalse(isLoading.value!!)

            assertFalse(sickLeaves.value?.isEmpty()!!)
            assertTrue(sickLeaves.value?.size == 4)
        }
    }

    @Test
    fun loadSickLeaves_dataNotAvailable() {
        viewModelCallback = mock(SickLeavesDataSource.LoadSickLeavesCallback::class.java)

        with(viewModel) {
            assertTrue(isLoading.value!!)

            verify<SickLeavesRepository>(repository)
                    .getSickLeaves(eq(TEST_USER_ID), capture(loadSickLeavesCallbackCaptor))
            loadSickLeavesCallbackCaptor.value.onDataNotAvailable()

            assertFalse(isLoading.value!!)
            assertTrue(sickLeaves.value?.isEmpty()!!)
        }
    }

    @Test
    fun loadSickLeaves_opened() {
        viewModelCallback = mock(SickLeavesDataSource.LoadSickLeavesCallback::class.java)

        with(viewModel) {
            assertTrue(isLoading.value!!)

            currentFiltering = FilterType.OPEN

            verify<SickLeavesRepository>(repository)
                    .getSickLeaves(eq(TEST_USER_ID), capture(loadSickLeavesCallbackCaptor))
            loadSickLeavesCallbackCaptor.value.onSickLeavesLoaded(this@SickLeavesViewModelTest.sickLeaves)

            assertFalse(isLoading.value!!)
            assertTrue(sickLeaves.value?.size == 3)
        }
    }

    @Test
    fun loadSickLeaves_closed() {
        viewModelCallback = mock(SickLeavesDataSource.LoadSickLeavesCallback::class.java)

        with(viewModel) {
            assertTrue(isLoading.value!!)

            currentFiltering = FilterType.CLOSE

            verify<SickLeavesRepository>(repository)
                    .getSickLeaves(eq(TEST_USER_ID), capture(loadSickLeavesCallbackCaptor))
            loadSickLeavesCallbackCaptor.value.onSickLeavesLoaded(this@SickLeavesViewModelTest.sickLeaves)

            assertFalse(isLoading.value!!)
            assertTrue(sickLeaves.value?.size == 1)
        }
    }

    @Test
    fun closeSickLeave_success() {
        viewModelCallback = mock(SickLeavesDataSource.LoadSickLeavesCallback::class.java)

        with(viewModel) {
            closeSickLeave(sickLeave)

            assertTrue(isLoading.value!!)

            verify<SickLeavesRepository>(repository).saveSickLeave(
                    eq(TEST_USER_ID),
                    capture(sickLeaveCaptor),
                    capture(saveSickLeavesCallbackCaptor))
            saveSickLeavesCallbackCaptor.value.onSickLeaveSaved()

            assertFalse(isLoading.value!!)
            assertThat<Int>(
                    viewModel.snackBarMessage.value?.getContentIfNotHandled(),
                    `is`(R.string.sick_list_close_success_message))
        }
    }

    @Test
    fun closeSickLeave_failed() {
        viewModelCallback = mock(SickLeavesDataSource.LoadSickLeavesCallback::class.java)

        with(viewModel) {
            closeSickLeave(sickLeave)

            assertTrue(isLoading.value!!)

            verify<SickLeavesRepository>(repository).saveSickLeave(
                    eq(TEST_USER_ID),
                    capture(sickLeaveCaptor),
                    capture(saveSickLeavesCallbackCaptor)
            )
            saveSickLeavesCallbackCaptor.value.onSickLeaveSaveFailed()

            assertFalse(isLoading.value!!)
            assertThat(
                    viewModel.snackBarMessage.value?.getContentIfNotHandled(),
                    `is`(R.string.sick_list_close_error_message)
            )
        }
    }

    @Test
    fun deleteSickLeave_success() {
        viewModelCallback = mock(SickLeavesDataSource.LoadSickLeavesCallback::class.java)

        with(viewModel) {
            deleteSickLeave(sickLeave)

            verify<SickLeavesRepository>(repository).deleteSickLeave(
                    eq(TEST_USER_ID),
                    eq(sickLeave.id.toString()),
                    capture(deleteSickLeaveCallbackCaptor)
            )
            deleteSickLeaveCallbackCaptor.value.onSickLeaveDeleted()

            assertThat(
                    viewModel.snackBarMessage.value?.getContentIfNotHandled(),
                    `is`(R.string.sick_list_delete_success_message))
        }
    }

    @Test
    fun deleteSickLeave_failed() {
        viewModelCallback = mock(SickLeavesDataSource.LoadSickLeavesCallback::class.java)

        with(viewModel) {
            deleteSickLeave(sickLeave)

            verify<SickLeavesRepository>(repository).deleteSickLeave(
                    eq(TEST_USER_ID),
                    eq(sickLeave.id.toString()),
                    capture(deleteSickLeaveCallbackCaptor)
            )
            deleteSickLeaveCallbackCaptor.value.onSickLeaveDeleteFailed()

            assertThat(
                    viewModel.snackBarMessage.value?.getContentIfNotHandled(),
                    `is` (R.string.sick_list_delete_error_message)
            )
        }
    }
}