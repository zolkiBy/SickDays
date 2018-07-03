package com.android.dev.yashchuk.sickleaves.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.callbacks.OnUserAuthListener
import com.android.dev.yashchuk.sickleaves.data.source.remote.net.AuthApi
import com.android.dev.yashchuk.sickleaves.utils.Event
import java.util.regex.Pattern

private const val PASSWORD_MIN_LENGTH = 6
private val PATTERN_EMAIL = Pattern.compile("\\b[\\w\\-_.]+@[a-zA-Z\\-_]+\\.[a-zA-Z]+\\b")

class LoginPresenter(private val view: LoginContract.View,
                     private val api: AuthApi) : LoginContract.Presenter {

    private val snackBarMessage = MutableLiveData<Event<Int>>()

    val message: LiveData<Event<Int>>
        get() = snackBarMessage

    override fun createUser(email: String, password: String) {
        if (isEmailPasswordValid(email, password)) {
            view.showProgress(true)
            api.createUser(email,
                    password,
                    object : OnUserAuthListener {
                        override fun onSuccess() {
                            view.showProgress(false)
                            view.openListScreen()
                        }

                        override fun onFailed() {
                            view.showProgress(false)
                            view.showError(R.string.login_error_create_user)
                        }
                    })
        }

    }

    override fun signIn(email: String, password: String) {
        if (isEmailPasswordValid(email, password)) {
            view.showProgress(true)
            api.signIn(email,
                    password,
                    object : OnUserAuthListener {
                        override fun onSuccess() {
                            view.showProgress(false)
                            view.openListScreen()
                        }

                        override fun onFailed() {
                            view.showProgress(false)
                            view.showError(R.string.login_error_sign_in)
                        }
                    })
        }
    }

    override fun isEmailValid(email: String): Boolean {
        return PATTERN_EMAIL.matcher(email).matches()
    }

    override fun isPasswordValid(password: String): Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }

    private fun isEmailPasswordValid(email: String, password: String): Boolean {
        return isEmailValid(email) && isPasswordValid(password)
    }
}