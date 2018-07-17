package com.android.dev.yashchuk.sickleaves.detail.addedit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.DatePickerCode
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status
import com.android.dev.yashchuk.sickleaves.detail.SickLeaveDetailViewModel
import com.android.dev.yashchuk.sickleaves.detail.datepicker.DatePickerFragment
import com.android.dev.yashchuk.sickleaves.utils.Event
import com.android.dev.yashchuk.sickleaves.utils.Injection
import com.android.dev.yashchuk.sickleaves.utils.getFormattedDate
import com.android.dev.yashchuk.sickleaves.utils.getFormattedDateString
import kotlinx.android.synthetic.main.fragment_add_edit_sick_leave.*
import java.util.*

private const val PARAM_USER_ID = "USER_ID"
private const val PARAM_SICK_LEAVE_ID = "SICK_LEAVE_ID"
private const val REQUEST_CODE_TARGET_FRAGMENT = 111
private const val TAG_DATE_PICKER = "DATE_PICKER"

class SickLeaveAddEditFragment : Fragment(), SickLeaveAddEditContract.View, DatePickerFragment.OnDateSetListener {
    private var userId: String? = null
    private var sickLeaveId: String? = null
    private var listener: OnCloseScreenListener? = null

    private var sickLeave: SickLeave? = null

    private lateinit var presenter: SickLeaveAddEditContract.Presenter
    private lateinit var viewModel: SickLeaveDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(PARAM_USER_ID)
            sickLeaveId = it.getString(PARAM_SICK_LEAVE_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_sick_leave, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = createViewModel()
        viewModel.loadSickLeave(sickLeaveId)

        presenter = initPresenter()

        subscribeUpdateLoadingState()

        subscribeUpdateSickLeave()

        subscribeSnackBarMessage()

        configButtons()

        configDateViews()
    }

    private fun initPresenter() = Injection.provideSickLeaveDetailPresenter(this)

    private fun createViewModel(): SickLeaveDetailViewModel {
        val viewModelFactory =
                Injection.provideSickLeaveDetailViewModelFactory(activity!!.applicationContext, userId)
        return ViewModelProviders.of(this, viewModelFactory)
                .get(SickLeaveDetailViewModel::class.java)
    }

    private fun subscribeUpdateLoadingState() {
        viewModel.isLoading.observe(this, Observer<Boolean> { isShow ->
            if (isShow == true) {
                presenter.showLoading(true)
            } else {
                presenter.showLoading(false)
            }
        })
    }

    private fun subscribeUpdateSickLeave() {
        viewModel.sickLeave.observe(activity!!, Observer<SickLeave> { sickLeave ->
            presenter.updateUi(sickLeave)
            this.sickLeave = sickLeave
        })
    }

    override fun showLoading(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun subscribeSnackBarMessage() {
        viewModel.snackBarMessage.observe(this, Observer<Event<Int>> {
            it?.getContentIfNotHandled()?.let { messageResId ->
                Snackbar.make(content, getString(messageResId), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun configButtons() {
        create_save_btn.setOnClickListener {
            val sickLeave = SickLeave(
                    id = Date().time,
                    title = title.text.toString(),
                    description = description.text.toString(),
                    startDate = start_date.text.toString().getFormattedDate(),
                    endDate = end_date.text.toString().getFormattedDate()
            )

            presenter.saveSickLeave(sickLeave)
            presenter.closeScreen()
        }

        close_btn.setOnClickListener {
            if (sickLeave != null) {
                sickLeave?.status = Status.CLOSE.name
                presenter.saveSickLeave(sickLeave!!)
            }
        }
    }

    private fun configDateViews() {
        start_date.setOnClickListener {
            presenter.showDatePicker(DatePickerCode.START_DATE_CODE.ordinal)
        }

        end_date.setOnClickListener {
            presenter.showDatePicker(DatePickerCode.END_DATE_CODE.ordinal)
        }
    }

    override fun onDateSet(requestCode: Int?, date: Date) {
        when (requestCode) {
            DatePickerCode.START_DATE_CODE.ordinal -> start_date.text = date.getFormattedDateString()
            DatePickerCode.END_DATE_CODE.ordinal -> end_date.text = date.getFormattedDateString()
        }
    }

    override fun fillSickLeaveData(sickLeave: SickLeave) {
        title.setText(sickLeave.title)
        description.setText(sickLeave.description)
        start_date.text = sickLeave.startDate?.getFormattedDateString()
        end_date.text = sickLeave.endDate?.getFormattedDateString()
                ?: getString(R.string.fragment_detail_sick_leave_end_date_text)
        create_save_btn.text = getString(R.string.fragment_detail_btn_save_text)
        close_btn.visibility = View.VISIBLE
    }

    override fun saveSickLeave(sickLeave: SickLeave) {
        viewModel.saveSickLeave(sickLeave)
    }

    override fun showEmptySickLeave() {
        start_date.text = Calendar.getInstance().time.getFormattedDateString()
        end_date.text = getString(R.string.fragment_detail_sick_leave_end_date_text)
        create_save_btn.text = getString(R.string.fragment_detail_btn_create_text)
        close_btn.visibility = View.GONE
    }

    override fun showDatePicker(requestCode: Int) {
        val datePicker = DatePickerFragment.newInstance(requestCode)
        datePicker.setTargetFragment(this, REQUEST_CODE_TARGET_FRAGMENT)
        datePicker.show(fragmentManager, TAG_DATE_PICKER)
    }

    override fun closeScreen() {
        listener?.onCloseScreen()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCloseScreenListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCloseScreenListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnCloseScreenListener {
        fun onCloseScreen()
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String?, sickLeaveId: String?) =
                SickLeaveAddEditFragment().apply {
                    arguments = Bundle().apply {
                        putString(PARAM_USER_ID, userId)
                        putString(PARAM_SICK_LEAVE_ID, sickLeaveId)
                    }
                }
    }
}
