package com.android.dev.yashchuk.sickleaves.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.StringRes
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.callbacks.OnUserAuthListener
import com.android.dev.yashchuk.sickleaves.data.source.remote.net.AuthApi
import com.android.dev.yashchuk.sickleaves.utils.Event


class LoginPresenter(private val view: LoginContract.View,
                     private val api: AuthApi) : LoginContract.Presenter {

    private val snackBarMessage = MutableLiveData<Event<Int>>()

    val message: LiveData<Event<Int>>
        get() = snackBarMessage

    override fun createUser(email: String, password: String) {
        api.createUser(email,
                password,
                object : OnUserAuthListener {
                    override fun onSuccess() {
                        view.openListScreen()
                    }

                    override fun onFailed() {
                        view.showError(R.string.login_error_create_user)
                    }
                })
    }

    override fun signIn(email: String, password: String) {
        api.signIn(email,
                password,
                object : OnUserAuthListener {
                    override fun onSuccess() {
                        view.openListScreen()
                    }

                    override fun onFailed() {
                        view.showError(R.string.login_error_sign_in)
                    }
                })
    }


    private fun showSnackbar(@StringRes resId: Int) {

    }
}