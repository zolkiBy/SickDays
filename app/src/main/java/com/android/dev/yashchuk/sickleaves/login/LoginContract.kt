package com.android.dev.yashchuk.sickleaves.login


interface LoginContract {
    interface View {
        fun showProgress(show: Boolean)
        fun openListScreen()
        fun showError(message: String)
    }

    interface Presenter {
        fun createUser(email: String, password: String)
        fun signIn(email: String, password: String)
    }
}