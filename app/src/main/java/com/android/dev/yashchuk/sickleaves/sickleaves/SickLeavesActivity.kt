package com.android.dev.yashchuk.sickleaves.sickleaves

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.utils.replaceFragmentInActivity
import com.android.dev.yashchuk.sickleaves.utils.setupActionBar

class SickLeavesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sick_leaves)

        setupActionBar(R.id.toolbar) {}

        findOrCreateFragment()
    }

    private fun findOrCreateFragment() =
            supportFragmentManager.findFragmentById(R.id.container)
                    ?: SickLeavesFragment.newInstance().also {
                        replaceFragmentInActivity(it, R.id.container)
                    }
}
