package com.android.dev.yashchuk.sickleaves.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.sickleaves.SickLeavesFragment
import com.android.dev.yashchuk.sickleaves.utils.replaceFragmentInActivity
import com.android.dev.yashchuk.sickleaves.utils.setupActionBar

import kotlinx.android.synthetic.main.activity_sick_leave_detail.*

// TODO: remove after add user id
private const val USER_ID = "21312312"

class SickLeaveDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sick_leave_detail)

        setupActionBar(R.id.toolbar) {
            // set title, icon etc. here
            setDisplayHomeAsUpEnabled(true)
        }

        findOrCreateFragment()
    }

    // TODO: change sickLeaveId
    private fun findOrCreateFragment() =
            supportFragmentManager.findFragmentById(R.id.container)
                    ?: SickLeaveDetailFragment.newInstance(USER_ID, "cfc7575757").also { fragment ->
                        replaceFragmentInActivity(fragment, R.id.container)
                    }
}
