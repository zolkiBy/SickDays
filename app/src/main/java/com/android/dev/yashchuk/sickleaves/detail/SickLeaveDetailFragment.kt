package com.android.dev.yashchuk.sickleaves.detail

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
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.utils.Injection
import com.android.dev.yashchuk.sickleaves.utils.getFormattedDate
import kotlinx.android.synthetic.main.fragment_sick_leave_detail.*
import java.util.*

private const val USER_ID_PARAM = "USER_ID"
private const val SICK_LEAVE_ID_PARAM = "SICK_LEAVE_ID"

class SickLeaveDetailFragment : Fragment(), SickLeaveDetailContract.View {
    private var userId: String? = null
    private var sickLeaveId: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var presenter: SickLeaveDetailContract.Presenter

    private lateinit var viewModel: SickLeaveDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID_PARAM)
            sickLeaveId = it.getString(SICK_LEAVE_ID_PARAM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sick_leave_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = createViewModel()
        viewModel.loadSickLeave(sickLeaveId!!)

        presenter = initPresenter()

        subscribeUpdateLoadingState()

        subscribeUpdateSickLeave()

        subscribeSnackBarMessage()
    }

    private fun initPresenter() = Injection.provideSickLeaveDetailPresenter(activity!!.applicationContext, this)

    private fun createViewModel(): SickLeaveDetailViewModel {
        val viewModelFactory =
                Injection.provideSickLeaveDetailViewModelFactory(activity!!.applicationContext, userId)
        return ViewModelProviders.of(activity!!, viewModelFactory)
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
            presenter.updateUi(sickLeave)
        })
    }

    private fun showLoading(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun subscribeSnackBarMessage() {
        viewModel.snackBarMessage.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { messageResId ->
                Snackbar.make(content, getString(messageResId), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun configBtns() {
        create_save_btn.setOnClickListener {
            val sickLeave = SickLeave(
                    title = title.text.toString(),
                    description = description.text.toString(),
                    startDate = Calendar.getInstance().time
            )
            viewModel.saveSickLeave(userId!!, sickLeave)
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    // TODO: remove if not need
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    override fun fillSickLeaveData(sickLeave: SickLeave) {
        title.setText(sickLeave.title)
        description.setText(sickLeave.description)
        start_date.text = sickLeave.startDate.getFormattedDate()
        end_date.text = sickLeave.endDate?.getFormattedDate()
        create_save_btn.text = getString(R.string.fragment_detail_btn_save_text)
        close_btn.visibility = View.VISIBLE
    }

    override fun showEmptySickLeave() {
        start_date.text = Calendar.getInstance().time.getFormattedDate()
        end_date.text = getString(R.string.fragment_detail_sick_leave_end_date_text)
        create_save_btn.text = getString(R.string.fragment_detail_btn_create_text)
        close_btn.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String, sickLeaveId: String?) =
                SickLeaveDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID_PARAM, userId)
                        putString(SICK_LEAVE_ID_PARAM, sickLeaveId)
                    }
                }
    }
}
