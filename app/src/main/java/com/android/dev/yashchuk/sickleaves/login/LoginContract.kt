package com.android.dev.yashchuk.sickleaves.login

import android.support.annotation.StringRes


interface LoginContract {
    interface View {
        fun showProgress(show: Boolean)
        fun openListScreen()
        fun showError(@StringRes resId: Int)
        fun showEmailError(@StringRes resId: Int)
        fun showPasswordError(@StringRes resId: Int)
    }

    interface Presenter {
        fun createUser(email: String?, password: String?)
        fun signIn(email: String?, password: String?)
        fun isEmailValid(email: String?): Boolean
    }
}