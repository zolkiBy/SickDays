package com.android.dev.yashchuk.sickleaves.data.source.remote.net

import android.support.annotation.VisibleForTesting
import android.util.Log
import com.android.dev.yashchuk.sickleaves.callbacks.OnUserAuthListener
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class FireBaseAuthApi : AuthApi{

    override fun createUser(email: String, password: String, callback: OnUserAuthListener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onSuccess()
            } else {
                callback.onFailed()
            }
        }
    }

    override fun signIn(email: String, password: String, callback: OnUserAuthListener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback.onSuccess()
            } else {
                callback.onFailed()
            }
        }
    }

    override fun getUser() : FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    companion object {
        private var INSTANCE: FireBaseAuthApi? = null

        @JvmStatic
        fun getInstance()
                : FireBaseAuthApi {
            INSTANCE ?: synchronized(FireBaseAuthApi::class.java) {
                INSTANCE ?: FireBaseAuthApi()
                        .also { firebaseAuthApi ->
                            INSTANCE = firebaseAuthApi
                        }
            }

            return INSTANCE!!
        }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}