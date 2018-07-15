package com.android.dev.yashchuk.sickleaves.login

import android.support.annotation.StringRes
import com.google.firebase.auth.FirebaseUser


interface LoginContract {
    interface View {
        fun showProgress(show: Boolean)
        fun openListScreen()
        fun showError(@StringRes resId: Int)
        fun showEmailError(@StringRes resId: Int)
        fun showPasswordError(@StringRes resId: Int)
        fun finishActivity()
        fun saveUserIdToPrefs(userId: String)
        fun loadUser()
    }

    interface Presenter {
        fun attemptCreateUser(email: String?, password: String?)
        fun attemptSignIn(email: String?, password: String?)
        fun checkUser(user: FirebaseUser?)
        fun saveUserIdToPrefs(userId: String?)
    }
}