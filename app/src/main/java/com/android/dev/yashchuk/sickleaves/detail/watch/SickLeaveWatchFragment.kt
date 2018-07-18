package com.android.dev.yashchuk.sickleaves.detail.watch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.callbacks.OnTitleChangeListener
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.detail.SickLeaveDetailViewModel
import com.android.dev.yashchuk.sickleaves.utils.Event
import com.android.dev.yashchuk.sickleaves.utils.Injection
import com.android.dev.yashchuk.sickleaves.utils.getFormattedDateString
import kotlinx.android.synthetic.main.fragment_sick_leave_watch.*

private const val PARAM_USER_ID = "USER_ID"
private const val PARAM_SICK_LEAVE_ID = "SICK_LEAVE_ID"

class SickLeaveWatchFragment : Fragment() {

    private var userId: String? = null
    private var sickLeaveId: String? = null
    private var sickLeave: SickLeave? = null

    private var titleChangeListener: OnTitleChangeListener? = null


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
        return inflater.inflate(R.layout.fragment_sick_leave_watch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = createViewModel()
        viewModel.loadSickLeave(sickLeaveId)

        subscribeUpdateLoadingState()
        subscribeUpdateSickLeave()
        subscribeSnackBarMessage()
    }

    private fun createViewModel(): SickLeaveDetailViewModel {
        val viewModelFactory =
                Injection.provideSickLeaveDetailViewModelFactory(activity!!.applicationContext, userId)
        return ViewModelProviders.of(this, viewModelFactory)
                .get(SickLeaveDetailViewModel::class.java)
    }

    private fun subscribeUpdateLoadingState() {
        viewModel.isLoading.observe(this, Observer<Boolean> { isShow ->
            if (isShow == true) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        })
    }

    private fun subscribeUpdateSickLeave() {
        viewModel.sickLeave.observe(activity!!, Observer<SickLeave> { sickLeave ->
            sickLeave?.let {
                fillSickLeaveData(it)
                titleChangeListener?.onTitleChange(it.title)
                this.sickLeave = it
            }
        })
    }

    private fun showLoading(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

    private fun subscribeSnackBarMessage() {
        viewModel.snackBarMessage.observe(this, Observer<Event<Int>> {
            it?.getContentIfNotHandled()?.let { messageResId ->
                Snackbar.make(content, getString(messageResId), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun fillSickLeaveData(sickLeave: SickLeave) {
        title.text = sickLeave.title
        description.text = sickLeave.description
        start_date.text = sickLeave.startDate?.getFormattedDateString()
        end_date.text = sickLeave.endDate?.getFormattedDateString()
                ?: getString(R.string.fragment_detail_sick_leave_end_date_text)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnTitleChangeListener) {
            titleChangeListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnTitleResChangeListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        titleChangeListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String?, sickLeaveId: String?) = SickLeaveWatchFragment().apply {
            arguments = Bundle().apply {
                putString(PARAM_USER_ID, userId)
                putString(PARAM_SICK_LEAVE_ID, sickLeaveId)
            }
        }
    }
}
