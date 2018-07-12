package com.android.dev.yashchuk.sickleaves.sickleaves

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.detail.addedit.SickLeaveDetailActivity
import com.android.dev.yashchuk.sickleaves.utils.getUserIdFromPrefs
import com.android.dev.yashchuk.sickleaves.utils.replaceFragmentInActivity
import com.android.dev.yashchuk.sickleaves.utils.setupActionBar
import kotlinx.android.synthetic.main.activity_sick_leaves.*

class SickLeavesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sick_leaves)

        setupActionBar(R.id.toolbar) {}

        val userId = getUserIdFromPrefs()

        findOrCreateFragment(userId)

        configCreateButton()
    }

    private fun findOrCreateFragment(userId: String?) =
            supportFragmentManager.findFragmentById(R.id.container)
                    ?: SickLeavesFragment.newInstance(userId).also {
                        replaceFragmentInActivity(it, R.id.container)
                    }

    private fun configCreateButton() {
        create_btn.setOnClickListener {
            SickLeaveDetailActivity.start(this, null, null)
        }
    }
}
