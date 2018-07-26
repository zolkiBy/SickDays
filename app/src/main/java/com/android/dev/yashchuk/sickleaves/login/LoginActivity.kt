package com.android.dev.yashchuk.sickleaves.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.NotificationChannel
import android.app.NotificationManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.source.remote.net.FireBaseAuthApi
import com.android.dev.yashchuk.sickleaves.sickleaves.SickLeavesActivity
import com.android.dev.yashchuk.sickleaves.utils.Injection
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = initPresenter()

        createViewModel()
        loadUser()

        configureViews()

        createNotificationChannel()
    }

    override fun onStart() {
        super.onStart()

        subscribeUpdateUser()
    }

    private fun initPresenter() =
            Injection.provideLoginPresenter(this, FireBaseAuthApi.getInstance())

    private fun configureViews() {
        sign_in_btn.setOnClickListener {
            presenter.attemptSignIn(email.text.toString(), password.text.toString())
        }

        register_btn.setOnClickListener {
            presenter.attemptCreateUser(email.text.toString(), password.text.toString())
        }
    }

    private fun createViewModel() {
        val viewModelFactory = Injection.provideLoginViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    private fun subscribeUpdateUser() {
        viewModel.user.observe(this, Observer<FirebaseUser> { user ->
            presenter.saveUserIdToPrefs(user?.uid)
            presenter.checkUser(user)
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW))
        }
    }

    override fun saveUserIdToPrefs(userId: String) {
        val sharedPref = this.getSharedPreferences(getString(R.string.preference_user_id_key), Context.MODE_PRIVATE)
                ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.preference_user_id_key), userId)
            apply()
        }
    }

    override fun loadUser() {
        viewModel.loadCurrentUser()
    }

    override fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    override fun openListScreen() {
        startActivity(Intent(this@LoginActivity,
                SickLeavesActivity::class.java))
    }

    override fun showError(@StringRes resId: Int) {
        Snackbar.make(login_form, getString(resId), Snackbar.LENGTH_SHORT).show()
    }

    override fun showEmailError(resId: Int) {
        email.error = getString(resId)
    }

    override fun showPasswordError(resId: Int) {
        password.error = getString(resId)
    }

    override fun finishActivity() {
        finish()
    }
}
