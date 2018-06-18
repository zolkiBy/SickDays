package com.android.dev.yashchuk.sickleaves.login

import com.android.dev.yashchuk.sickleaves.data.source.remote.net.AuthApi
import com.google.firebase.auth.FirebaseUser


class LoginPresenter(private val view: LoginContract.View,
                     private val user: FirebaseUser,
                     private val api: AuthApi) {



}