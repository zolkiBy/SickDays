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
        if (isEmailPasswordValid(email, password)) {
            createUser(email, password)
        } else {
            showEmailError(email)
            showPasswordError(password)
        }
    }

    override fun attemptSignIn(email: String?, password: String?) {
        if (isEmailPasswordValid(email, password)) {
            signIn(email, password)
        } else {
            showEmailError(email)
            showPasswordError(password)
        }
    }

    override fun checkUser(user: FirebaseUser?) {
        if (user != null) {
            openListScreenAndFinish()
        }
    }

    private fun createUser(email: String?, password: String?) {
        view.showProgress(true)
        api.createUser(
                email!!,
                password!!,
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

    override fun saveUserIdToPrefs(userId: String?) {
        userId?.let {
            view.saveUserIdToPrefs(it)
        }
    }

    private fun showPasswordError(password: String?) {
        if (!password.isPasswordValid()) view.showPasswordError(R.string.error_invalid_password)
    }

    private fun showEmailError(email: String?) {
        if (email == null || email.isBlank()) {
            view.showEmailError(R.string.error_field_required)
        } else if (!email.isEmailValid()) {
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

    private fun isEmailPasswordValid(email: String?, password: String?): Boolean {
        return email.isEmailValid() && password.isPasswordValid()
    }

    private fun String?.isPasswordValid(): Boolean {
        return this != null && !this.isBlank() && this.length >= PASSWORD_MIN_LENGTH
    }

    private fun String?.isEmailValid(): Boolean {
        return PATTERN_EMAIL.matcher(this).matches()
    }
}