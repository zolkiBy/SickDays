package com.android.dev.yashchuk.sickleaves.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.capture
import com.android.dev.yashchuk.sickleaves.data.SickLeave
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
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*


private const val TEST_USER_ID = "nskncsd-324mcmdks-cdsccslc"

class SickLeaveDetailViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: SickLeavesRepository
    @Captor
    private lateinit var getSickLeaveCallbackCaptor: ArgumentCaptor<SickLeavesDataSource.GetSickLeaveCallback>
    @Captor
    private lateinit var saveSickLeaveCallbackCaptor: ArgumentCaptor<SickLeavesDataSource.SaveSickLeaveCallback>
    @Captor
    private lateinit var sickLeaveCaptor: ArgumentCaptor<SickLeave>

    private lateinit var viewModel: SickLeaveDetailViewModel

    private lateinit var sickLeave: SickLeave

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = SickLeaveDetailViewModel(TEST_USER_ID, repository)

        sickLeave = SickLeave(
                id = Date().time,
                title = "Test title",
                description = "Test description"
        )
    }

    @Test
    fun getSickLeave_dataLoaded() {
        with(viewModel) {
            loadSickLeave(this@SickLeaveDetailViewModelTest.sickLeave.id.toString())

            assertTrue(isLoading.value!!)
            verify<SickLeavesRepository>(repository).getSickLeave(
                    eq(TEST_USER_ID),
                    eq(this@SickLeaveDetailViewModelTest.sickLeave.id.toString()),
                    capture(getSickLeaveCallbackCaptor))


            getSickLeaveCallbackCaptor.value.onSickLeaveLoaded(this@SickLeaveDetailViewModelTest.sickLeave)

            assertFalse(isLoading.value!!)
            assertTrue(sickLeave.value == this@SickLeaveDetailViewModelTest.sickLeave)
        }
    }

    @Test
    fun getSickLeave_dataNotAvailable() {
        with(viewModel) {
            loadSickLeave(this@SickLeaveDetailViewModelTest.sickLeave.id.toString())

            assertTrue(isLoading.value!!)
            verify<SickLeavesRepository>(repository).getSickLeave(
                    eq(TEST_USER_ID),
                    eq(this@SickLeaveDetailViewModelTest.sickLeave.id.toString()),
                    capture(getSickLeaveCallbackCaptor))

            getSickLeaveCallbackCaptor.value.onDataNotAvailable()

            assertFalse(isLoading.value!!)
            assertTrue(sickLeave.value == null)
            assertThat(
                    snackBarMessage.value?.getContentIfNotHandled(),
                    `is`(R.string.fragment_detail_failed_load_sick_leave)
            )
        }
    }

    @Test
    fun saveSickLeave_saved() {
        with(viewModel) {
            saveSickLeave(this@SickLeaveDetailViewModelTest.sickLeave)

            assertTrue(isLoading.value!!)
            verify<SickLeavesRepository>(repository).saveSickLeave(
                    eq(TEST_USER_ID),
                    capture(sickLeaveCaptor),
                    capture(saveSickLeaveCallbackCaptor))

            saveSickLeaveCallbackCaptor.value.onSickLeaveSaved()

            assertFalse(isLoading.value!!)
            assertTrue(sickLeave.value == this@SickLeaveDetailViewModelTest.sickLeave)
        }
    }

    @Test
    fun saveSickLeave_failed() {
        with(viewModel) {
            saveSickLeave(this@SickLeaveDetailViewModelTest.sickLeave)

            assertTrue(isLoading.value!!)
            verify<SickLeavesRepository>(repository).saveSickLeave(
                    eq(TEST_USER_ID),
                    capture(sickLeaveCaptor),
                    capture(saveSickLeaveCallbackCaptor))

            saveSickLeaveCallbackCaptor.value.onSickLeaveSaveFailed()

            assertFalse(isLoading.value!!)
            assertThat(
                    snackBarMessage.value?.getContentIfNotHandled(),
                    `is`(R.string.fragment_detail_failed_save_sick_leave)
            )
        }
    }
}