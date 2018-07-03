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

    fun addSickLeave(fireStore: FirebaseFirestore) {
        val sickLeave = SickLeave(title = "Title", description = "description")
        val sickLeaveObj = HashMap<String, Any>()
        sickLeaveObj["title"] = sickLeave.title
        sickLeaveObj["description"] = sickLeave.description
        sickLeaveObj["id"] = sickLeave.id
        fireStore.collection("sickleaves")
                .add(sickLeaveObj)
                .addOnSuccessListener { Log.d("FireBaseAuthApi", "Add document" + it.id) }
                .addOnFailureListener { Log.w("FireBaseAuthApi", "Error", it) }
    }

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