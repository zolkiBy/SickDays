package com.android.dev.yashchuk.sickleaves.data.source.remote.net

import com.android.dev.yashchuk.sickleaves.callbacks.OnUserAuthListener
import com.google.firebase.auth.FirebaseUser

interface AuthApi {
    fun createUser(email: String, password: String, callback: OnUserAuthListener)
    fun signIn(email: String, password: String, callback: OnUserAuthListener)
    fun signOut()
    fun getUser(): FirebaseUser?
}