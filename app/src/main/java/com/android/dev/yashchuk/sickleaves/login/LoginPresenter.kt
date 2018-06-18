package com.android.dev.yashchuk.sickleaves.login

import com.android.dev.yashchuk.sickleaves.callbacks.OnUserAuthListener
import com.android.dev.yashchuk.sickleaves.data.source.remote.net.AuthApi


class LoginPresenter(private val view: LoginContract.View,
                     private val api: AuthApi) : LoginContract.Presenter {

    override fun createUser(email: String, password: String) {
        api.createUser(email,
                password,
                object : OnUserAuthListener {
                    override fun onSuccess() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onFailed() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }

    override fun signIn(email: String, password: String) {
        api.signIn(email,
                password,
                object : OnUserAuthListener {
                    override fun onSuccess() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onFailed() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }


}