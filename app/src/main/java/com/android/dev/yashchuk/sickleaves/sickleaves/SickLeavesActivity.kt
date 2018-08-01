package com.android.dev.yashchuk.sickleaves.sickleaves

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.callbacks.OnBackPressListener
import com.android.dev.yashchuk.sickleaves.callbacks.OnCloseScreenListener
import com.android.dev.yashchuk.sickleaves.callbacks.OnTitleResChangeListener
import com.android.dev.yashchuk.sickleaves.detail.SickLeaveDetailActivity
import com.android.dev.yashchuk.sickleaves.utils.getUserIdFromPrefs
import com.android.dev.yashchuk.sickleaves.utils.setupActionBar
import kotlinx.android.synthetic.main.activity_sick_leaves.*

class SickLeavesActivity :
        AppCompatActivity(),
        OnTitleResChangeListener,
        OnCloseScreenListener {

    private var backPressListener: OnBackPressListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sick_leaves)

        setupActionBar(R.id.toolbar) {
            toolbar.overflowIcon =
                    ContextCompat.getDrawable(this@SickLeavesActivity, R.drawable.ic_sort)
            setTitle(R.string.sick_list_toolbar_title_all)
        }

        val userId = getUserIdFromPrefs()

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SickLeavesFragment.newInstance(userId))
                    .commit()
        }

        configCreateButton()
    }

    private fun configCreateButton() {
        create_btn.setOnClickListener {
            SickLeaveDetailActivity.start(this, null, null)
        }
    }

    override fun onTitleResChange(titleResId: Int) {
        toolbar.title = getString(titleResId)
    }

    override fun onBackPressed() {
        backPressListener?.onBackPressed()
    }

    override fun onCloseScreen() {
        finish()
    }

    fun setBackPressedListener(backPressListener: OnBackPressListener) {
        this.backPressListener = backPressListener
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressListener = null
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, SickLeavesActivity::class.java)
            context.startActivity(intent)
        }
    }
}
