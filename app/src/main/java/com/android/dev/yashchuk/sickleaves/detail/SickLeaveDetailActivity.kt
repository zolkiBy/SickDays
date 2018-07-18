package com.android.dev.yashchuk.sickleaves.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.callbacks.OnCloseScreenListener
import com.android.dev.yashchuk.sickleaves.data.Status
import com.android.dev.yashchuk.sickleaves.detail.addedit.SickLeaveAddEditFragment
import com.android.dev.yashchuk.sickleaves.detail.watch.SickLeaveWatchFragment
import com.android.dev.yashchuk.sickleaves.utils.getUserIdFromPrefs
import com.android.dev.yashchuk.sickleaves.utils.setupActionBar

private const val EXTRA_SICK_LEAVE_ID = "SICK_LEAVE_ID"
private const val EXTRA_SICK_LEAVE_IS_EDIT = "SICK_LEAVE_STATUS"

class SickLeaveDetailActivity : AppCompatActivity(), OnCloseScreenListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_sick_leave)

        val userId = getUserIdFromPrefs()
        val sickLeaveId = intent.extras.getString(EXTRA_SICK_LEAVE_ID)
        val status = intent.extras.getString(EXTRA_SICK_LEAVE_IS_EDIT)

        setupActionBar(R.id.toolbar) {
            // set title, icon etc. here
            setDisplayHomeAsUpEnabled(true)
        }

        if (savedInstanceState == null) {
            val fragment = getFragment(userId, sickLeaveId, status)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()
        }
    }

    private fun getFragment(userId: String?, sickLeaveId: String?, status: String?): Fragment {
        return when (status) {
            Status.CLOSE.name -> SickLeaveWatchFragment.newInstance(userId, sickLeaveId)
            else -> SickLeaveAddEditFragment.newInstance(userId, sickLeaveId)
        }
    }

    override fun onCloseScreen() {
        finish()
    }

    companion object {
        @JvmStatic
        fun start(context: Context, sickLeaveId: String?, status: String?) {
            val intent = Intent(context, SickLeaveDetailActivity::class.java).apply {
                putExtra(EXTRA_SICK_LEAVE_ID, sickLeaveId)
                putExtra(EXTRA_SICK_LEAVE_IS_EDIT, status)
            }

            context.startActivity(intent)
        }
    }
}
