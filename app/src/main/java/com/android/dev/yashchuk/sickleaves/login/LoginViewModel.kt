package com.android.dev.yashchuk.sickleaves.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.dev.yashchuk.sickleaves.data.source.remote.net.AuthApi
import com.google.firebase.auth.FirebaseUser


class LoginViewModel(api: AuthApi) : ViewModel() {
    val user = MutableLiveData<FirebaseUser>()

    init {
        user.value = api.getUser()
    }
}