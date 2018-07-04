package com.android.dev.yashchuk.sickleaves.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.StringRes
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

    override fun createUser(email: String?, password: String?) {
        if (isEmailPasswordValid(email, password)) {
            view.showProgress(true)
            api.createUser(email!!,
                    password!!,
                    object : OnUserAuthListener {
                        override fun onSuccess() {
                            openListScreen()
                        }

                        override fun onFailed() {
                            showResponseError(R.string.login_error_create_user)
                        }
                    })
        } else {
            showEmailError(email)
            showPasswordError(password)
        }

    }

    override fun signIn(email: String?, password: String?) {
        if (password.isPasswordValid())
        if (isEmailPasswordValid(email, password)) {
            view.showProgress(true)
            api.signIn(email!!,
                    password!!,
                    object : OnUserAuthListener {
                        override fun onSuccess() {
                            openListScreen()
                        }

                        override fun onFailed() {
                            showResponseError(R.string.login_error_sign_in)
                        }
                    })
        } else {
            showEmailError(email)
            showPasswordError(password)
        }
    }

    override fun isEmailValid(email: String?): Boolean {
        return PATTERN_EMAIL.matcher(email).matches()
    }

    private fun isEmailPasswordValid(email: String?, password: String?): Boolean {
        return isEmailValid(email) && password.isPasswordValid()
    }

    private fun showPasswordError(password: String?) {
        if (!password.isPasswordValid()) view.showPasswordError(R.string.error_invalid_password)
    }

    private fun showEmailError(email: String?) {
        if (email == null || email.isBlank()) {
           view.showEmailError(R.string.error_field_required)
        } else if (!isEmailValid(email)) {
            view.showEmailError(R.string.error_invalid_email)
        }
    }

    private fun openListScreen() {
        view.showProgress(false)
        view.openListScreen()
    }

    private fun showResponseError(@StringRes resId: Int) {
        view.showProgress(false)
        view.showError(resId)
    }

    private fun String?.isPasswordValid(): Boolean {
        return this != null && !this.isBlank() && this.length >= PASSWORD_MIN_LENGTH
    }
}