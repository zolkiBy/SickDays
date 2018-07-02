package com.android.dev.yashchuk.sickleaves.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.utils.Injection

private const val USER_ID_PARAM = "USER_ID"
private const val SICK_LEAVE_ID_PARAM = "SICK_LEAVE_ID"

class SickLeaveDetailFragment : Fragment() {
    private var userId: String? = null
    private var sickLeaveId: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var viewModel: SickLeaveDetailViewModel

    private lateinit var progress: ProgressBar

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

        bindViews(view)

        viewModel = createViewModel()

        subscribeUpdateLoadingState()

        subscribeUpdateSickLeave()
    }

    private fun bindViews(view: View) {
        with(view) {
            progress = findViewById<View>(R.id.progress) as ProgressBar
        }
    }

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
        viewModel.loadSickLeave(sickLeaveId!!).observe(activity!!, Observer<SickLeave> { sickLeave ->
            updateUi(sickLeave)
        })
    }

    private fun updateUi(sickLeave: SickLeave?) {

    }

    private fun showLoading(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
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

    companion object {
        @JvmStatic
        fun newInstance(userId: String, sickLeaveId: String) =
                SickLeaveDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID_PARAM, userId)
                        putString(SICK_LEAVE_ID_PARAM, sickLeaveId)
                    }
                }
    }
}
