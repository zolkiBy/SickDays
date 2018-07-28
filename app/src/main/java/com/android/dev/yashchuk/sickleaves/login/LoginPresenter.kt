package com.android.dev.yashchuk.sickleaves.login

import android.support.annotation.StringRes
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.callbacks.OnUserAuthListener
import com.android.dev.yashchuk.sickleaves.data.source.remote.net.AuthApi
import com.google.firebase.auth.FirebaseUser
import java.util.regex.Pattern

private const val PASSWORD_MIN_LENGTH = 6
private val PATTERN_EMAIL = Pattern.compile("\\b[\\w\\-_.]+@[a-zA-Z\\-_]+\\.[a-zA-Z]+\\b")

class LoginPresenter(private val view: LoginContract.View,
                     private val api: AuthApi) : LoginContract.Presenter {

    override fun attemptCreateUser(email: String?, password: String?) {
        if (isEmailValid(email) && isPasswordValid(password)) {
            createUser(email!!, password!!)
        } else if (isEmailValid(email) && !isPasswordValid(password)) {
            showPasswordError()
        } else if (!isEmailValid(email) && isPasswordValid(password)) {
            showEmailError(email)
        } else if (!isEmailValid(email) && !isPasswordValid(password)) {
            showEmailError(email)
            showPasswordError()
        }
    }

    override fun attemptSignIn(email: String?, password: String?) {
        if (isEmailValid(email) && isPasswordValid(password)) {
            signIn(email, password)
        } else if (isEmailValid(email) && !isPasswordValid(password)) {
            showPasswordError()
        } else if (!isEmailValid(email) && isPasswordValid(password)) {
            showEmailError(email)
        } else if (!isEmailValid(email) && !isPasswordValid(password)) {
            showEmailError(email)
            showPasswordError()
        }
    }

    override fun checkUser(user: FirebaseUser?) {
        if (user != null) {
            openListScreenAndFinish()
        }
    }

    override fun saveUserIdToPrefs(userId: String?) {
        userId?.let {
            view.saveUserIdToPrefs(it)
        }
    }

    private fun createUser(email: String, password: String) {
        view.showProgress(true)
        api.createUser(
                email,
                password,
                object : OnUserAuthListener {
                    override fun onSuccess() {
                        openListScreenAndFinish()
                    }

                    override fun onFailed() {
                        showResponseError(R.string.login_error_create_user)
                    }
                })
    }

    private fun signIn(email: String?, password: String?) {
        view.showProgress(true)
        api.signIn(
                email!!,
                password!!,
                object : OnUserAuthListener {
                    override fun onSuccess() {
                        openListScreenAndFinish()
                        view.loadUser()
                    }

                    override fun onFailed() {
                        showResponseError(R.string.login_error_sign_in)
                    }
                })
    }

    private fun showPasswordError() {
        view.showPasswordError(R.string.error_invalid_password)
    }

    private fun showEmailError(email: String?) {
        if (email == null || email.isBlank()) {
            view.showEmailError(R.string.error_field_required)
        } else if (!isEmailValid(email)) {
            view.showEmailError(R.string.error_invalid_email)
        }
    }

    private fun showResponseError(@StringRes resId: Int) {
        view.showProgress(false)
        view.showError(resId)
    }

    private fun openListScreenAndFinish() {
        view.openListScreen()
        view.finishActivity()
    }

    private fun isEmailValid(email: String?): Boolean {
        return email != null
                && PATTERN_EMAIL.matcher(email).matches()
    }

    private fun isPasswordValid(password: String?): Boolean {
        return password != null
                && !password.isBlank()
                && password.length >= PASSWORD_MIN_LENGTH
    }
}