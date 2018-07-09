package com.android.dev.yashchuk.sickleaves.sickleaves


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.sickleaves.recycler.SickLeavesAdapter
import com.android.dev.yashchuk.sickleaves.utils.Injection
import kotlinx.android.synthetic.main.fragment_sick_leaves.*

private const val PARAM_USER_ID = "USER_ID"

class SickLeavesFragment : Fragment() {

    var userId: String? = null

    private lateinit var adapter: SickLeavesAdapter

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

        viewModel = createViewModel()

        subscribeUpdateLoadingState()

        subscribeUpdateSickLeaves()

        setupRecycler()
    }

    private fun createViewModel(): SickLeavesViewModel {
        val viewModelFactory =
                Injection.provideSickLeavesViewModelFactory(activity!!.applicationContext, userId)
        return ViewModelProviders.of(activity!!, viewModelFactory).get(SickLeavesViewModel::class.java)
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

    private fun subscribeUpdateSickLeaves() {
        viewModel.sickLeaves.observe(this, Observer<List<SickLeave>> { sickLeaves ->
            updateUi(sickLeaves)
        })
    }

    private fun updateUi(sickLeaves: List<SickLeave>?) {
        adapter.submitList(sickLeaves)
    }

    private fun showLoading(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
        recycler.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun setupRecycler() {
        adapter = SickLeavesAdapter(activity!!)
        recycler.apply {
            adapter = this@SickLeavesFragment.adapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    companion object {
        fun newInstance(userId: String?) = SickLeavesFragment().apply {
            arguments = Bundle().apply {
                putString(PARAM_USER_ID, userId)
            }
        }
    }
}
