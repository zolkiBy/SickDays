package com.android.dev.yashchuk.sickleaves.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.dev.yashchuk.sickleaves.data.source.remote.net.AuthApi
import com.google.firebase.auth.FirebaseUser


class LoginViewModel(private val api: AuthApi) : ViewModel() {
    private val _user = MutableLiveData<FirebaseUser>()

    val user: LiveData<FirebaseUser>
        get() = _user

    fun loadCurrentUser() {
        _user.value = api.getUser()
    }
}