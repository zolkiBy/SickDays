package com.android.dev.yashchuk.sickleaves.login

import com.android.dev.yashchuk.sickleaves.callbacks.OnUserAuthListener
import com.google.firebase.auth.FirebaseUser


interface LoginContract {
    interface View {
        fun showProgress(show: Boolean)
    }

    interface Presenter {
        fun createUser(email: String, password: String, callback: OnUserAuthListener)
        fun signIn(email: String, password: String, callback: OnUserAuthListener)
        fun getUser(): FirebaseUser?
    }
}