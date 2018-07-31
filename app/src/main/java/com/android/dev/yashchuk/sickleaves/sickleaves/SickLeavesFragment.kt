package com.android.dev.yashchuk.sickleaves.sickleaves

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.callbacks.OnBackPressListener
import com.android.dev.yashchuk.sickleaves.callbacks.OnCloseScreenListener
import com.android.dev.yashchuk.sickleaves.callbacks.OnTitleResChangeListener
import com.android.dev.yashchuk.sickleaves.data.FilterType
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.detail.SickLeaveDetailActivity
import com.android.dev.yashchuk.sickleaves.login.LoginActivity
import com.android.dev.yashchuk.sickleaves.sickleaves.recycler.SickLeavesAdapter
import com.android.dev.yashchuk.sickleaves.utils.Event
import com.android.dev.yashchuk.sickleaves.utils.Injection
import kotlinx.android.synthetic.main.fragment_sick_leaves.*

private const val PARAM_USER_ID = "USER_ID"

class SickLeavesFragment :
        Fragment(),
        SickLeavesContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        OnBackPressListener {

    var userId: String? = null

    private var titleResChangeListener: OnTitleResChangeListener? = null
    private var closeScreenListener: OnCloseScreenListener? = null

    private lateinit var adapter: SickLeavesAdapter

    private lateinit var presenter: SickLeavesContract.Presenter
    private lateinit var viewModel: SickLeavesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        subscribeToolbarTitle()

        setupRecycler()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSickLeaves(false, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_filter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
            when (item?.itemId) {
                R.id.all -> {
                    presenter.showAll()
                    true
                }
                R.id.opened -> {
                    presenter.showOpened()
                    true
                }
                R.id.closed -> {
                    presenter.showClosed()
                    true
                }
                R.id.log_token -> {
                    presenter.logFcmToken()
                    true
                }
                else -> false
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

    private fun subscribeToolbarTitle() {
        viewModel.toolbarTitleResId.observe(this, Observer<Int> { titleResId ->
            titleResId?.let {
                titleResChangeListener?.onTitleResChange(it)
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
        viewModel.closeSickLeave(sickLeave)
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

    override fun showData() {
        recycler.visibility = View.VISIBLE
    }

    override fun hideData() {
        recycler.visibility = View.GONE
    }

    override fun showLoading(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
        recycler.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showAll() {
        viewModel.currentFiltering = FilterType.ALL
        viewModel.loadSickLeaves(false, false)
    }

    override fun showOpened() {
        viewModel.currentFiltering = FilterType.OPEN
        viewModel.loadSickLeaves(false, false)
    }

    override fun showClosed() {
        viewModel.currentFiltering = FilterType.CLOSE
        viewModel.loadSickLeaves(false, false)
    }

    override fun logToken(token: String) {
        Log.d(SickLeavesFragment::class.java.simpleName, "FCM token: $token")
    }

    override fun showSignOutDialog() {
        AlertDialog.Builder(context!!)
                .setMessage(R.string.sick_list_sign_out_dialog_message)
                .setPositiveButton(R.string.sick_list_sign_out_dialog_positive_btn)
                { _, _ ->
                    run {
                        presenter.signOut()
                        closeScreenListener?.onCloseScreen()
                    }
                }
                .setNegativeButton(R.string.sick_list_sign_out_dialog_negative_btn) { dialog, _ -> dialog.dismiss() }
                .create()
    }

    override fun openLoginScreen() {
        LoginActivity.start(context!!)
    }

    override fun onRefresh() {
        viewModel.loadSickLeaves(true, true)
    }

    override fun onBackPressed() {
        presenter.showSignOutDialog()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnTitleResChangeListener) {
            titleResChangeListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnTitleResChangeListener")
        }

        if (context is OnCloseScreenListener) {
            closeScreenListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCloseScreenListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        titleResChangeListener = null
        closeScreenListener = null
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
