package com.android.dev.yashchuk.sickleaves.sickleaves

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.detail.SickLeaveDetailActivity
import com.android.dev.yashchuk.sickleaves.sickleaves.recycler.SickLeavesAdapter
import com.android.dev.yashchuk.sickleaves.utils.Event
import com.android.dev.yashchuk.sickleaves.utils.Injection
import kotlinx.android.synthetic.main.fragment_sick_leaves.*

private const val PARAM_USER_ID = "USER_ID"

class SickLeavesFragment :
        Fragment(),
        SickLeavesContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    var userId: String? = null

    private lateinit var adapter: SickLeavesAdapter

    private lateinit var presenter: SickLeavesContract.Presenter
    private lateinit var viewModel: SickLeavesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(PARAM_USER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sick_leaves, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = initPresenter()
        viewModel = createViewModel()

        initSwipeRefreshLayout()

        subscribeUpdateLoadingState()

        subscribeSwipeLoadingState()

        subscribeUpdateSickLeaves()

        subscribeSnackBarMessage()

        setupRecycler()
    }

    private fun initPresenter() = Injection.provideSickLeavesPresenter(this)

    private fun createViewModel(): SickLeavesViewModel {
        val viewModelFactory =
                Injection.provideSickLeavesViewModelFactory(activity!!.applicationContext, userId)
        return ViewModelProviders.of(activity!!, viewModelFactory).get(SickLeavesViewModel::class.java)
    }

    private fun initSwipeRefreshLayout() {
        swipe_refresh_layout.setOnRefreshListener(this)
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

    private fun subscribeSwipeLoadingState() {
        viewModel.isLoadingFromSwipe.observe(this, Observer<Boolean> { isLoading ->
            isLoading?.let {
                swipe_refresh_layout.isRefreshing = it
            }
        })
    }

    private fun subscribeUpdateSickLeaves() {
        viewModel.sickLeaves.observe(this, Observer<List<SickLeave>> { sickLeaves ->
            val sortedList = sickLeaves
                    ?.sortedWith(compareBy(SickLeave::status, SickLeave::startDate))?.reversed()

            presenter.updateUi(sortedList)
        })
    }

    private fun subscribeSnackBarMessage() {
        viewModel.snackBarMessage.observe(this, Observer<Event<Int>> {
            it?.getContentIfNotHandled()?.let { messageResId ->
                Snackbar.make(recycler, getString(messageResId), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecycler() {
        adapter = SickLeavesAdapter(
                activity!!,
                { sickLeave -> SickLeaveDetailActivity.start(activity!!, sickLeave.id.toString(), sickLeave.status) },
                { sickLeave -> presenter.closeSickLeave(sickLeave) },
                { sickLeave -> presenter.deleteSickLeave(sickLeave) })

        recycler.apply {
            adapter = this@SickLeavesFragment.adapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun updateUi(sickLeaves: List<SickLeave>?) {
        adapter.submitList(sickLeaves)
    }

    override fun closeSickLeave(sickLeave: SickLeave) {
        viewModel.saveSickLeave(sickLeave)
    }

    override fun deleteSickLeave(sickLeave: SickLeave) {
        viewModel.deleteSickLeave(sickLeave)
    }

    override fun showEmptyView() {
        empty_view.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        empty_view.visibility = View.GONE
    }

    override fun showDataList() {
        recycler.visibility = View.VISIBLE
    }

    override fun hideDataList() {
        recycler.visibility = View.GONE
    }

    override fun showLoading(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
        recycler.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun onRefresh() {
        viewModel.loadSickLeaves(true, true)
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String?) = SickLeavesFragment().apply {
            arguments = Bundle().apply {
                putString(PARAM_USER_ID, userId)
            }
        }
    }
}
