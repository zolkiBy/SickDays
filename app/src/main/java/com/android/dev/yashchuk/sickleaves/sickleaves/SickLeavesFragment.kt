package com.android.dev.yashchuk.sickleaves.sickleaves


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.android.dev.yashchuk.sickleaves.R

class SickLeavesFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sick_leaves, container, false)
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
