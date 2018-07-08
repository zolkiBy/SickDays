package com.android.dev.yashchuk.sickleaves.sickleaves

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.detail.SickLeaveDetailActivity
import com.android.dev.yashchuk.sickleaves.utils.replaceFragmentInActivity
import com.android.dev.yashchuk.sickleaves.utils.setupActionBar
import kotlinx.android.synthetic.main.activity_sick_leaves.*

class SickLeavesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sick_leaves)

        setupActionBar(R.id.toolbar) {}

        findOrCreateFragment()

        configCreateButton()
    }

    private fun findOrCreateFragment() =
            supportFragmentManager.findFragmentById(R.id.container)
                    ?: SickLeavesFragment.newInstance().also {
                        replaceFragmentInActivity(it, R.id.container)
                    }

    private fun configCreateButton() {
        create_btn.setOnClickListener {
            SickLeaveDetailActivity.start(this, null)
        }
    }
}
