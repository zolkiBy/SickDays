package com.android.dev.yashchuk.sickleaves.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.sickleaves.SickLeavesFragment
import com.android.dev.yashchuk.sickleaves.utils.getUserIdFromPrefs
import com.android.dev.yashchuk.sickleaves.utils.replaceFragmentInActivity
import com.android.dev.yashchuk.sickleaves.utils.setupActionBar

import kotlinx.android.synthetic.main.activity_sick_leave_detail.*

private const val EXTRA_SICK_LEAVE_ID = "SICK_LEAVE_ID"

class SickLeaveDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sick_leave_detail)

        val userId = getUserIdFromPrefs()
        val sickLeaveId = intent.extras.getString(EXTRA_SICK_LEAVE_ID)

        setupActionBar(R.id.toolbar) {
            // set title, icon etc. here
            setDisplayHomeAsUpEnabled(true)
        }

        findOrCreateFragment(userId, sickLeaveId)
    }

    private fun findOrCreateFragment(userId: String?, sickLeaveId: String?) =
            supportFragmentManager.findFragmentById(R.id.container)
                    ?: SickLeaveDetailFragment.newInstance(userId, sickLeaveId).also { fragment ->
                        replaceFragmentInActivity(fragment, R.id.container)
                    }

    companion object {
        @JvmStatic
        fun start(context: Context, sickLeaveId: String?) {
            val intent = Intent(context, SickLeaveDetailActivity::class.java)
            intent.putExtra(EXTRA_SICK_LEAVE_ID, sickLeaveId)
            context.startActivity(intent)
        }
    }
}
