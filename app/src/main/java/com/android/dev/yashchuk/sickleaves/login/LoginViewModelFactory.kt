package com.android.dev.yashchuk.sickleaves.login

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.dev.yashchuk.sickleaves.data.source.remote.net.AuthApi

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val api: AuthApi) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(api) as T
    }
}