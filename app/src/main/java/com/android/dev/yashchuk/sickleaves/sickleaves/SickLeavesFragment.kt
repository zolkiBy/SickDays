package com.android.dev.yashchuk.sickleaves.sickleaves


import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.sickleaves.recycler.SickLeavesAdapter
import com.android.dev.yashchuk.sickleaves.utils.Injection
import kotlinx.android.synthetic.main.fragment_sick_leaves.*

class SickLeavesFragment : Fragment() {

    // TODO - change to real user id
    val userId = "useerId"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sick_leaves, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = Injection.provideSickLeavesViewModelFactory(activity!!.applicationContext, userId)
    }

    private fun loadData() {

    }

    private fun setupRecycler() {
        val sickLeavesAdapter = SickLeavesAdapter(activity!!)
        recycler.apply {
            adapter = sickLeavesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        fun newInstance() = SickLeavesFragment().apply {
            arguments = Bundle().apply {
                // put arguments here
            }
        }
    }
}
