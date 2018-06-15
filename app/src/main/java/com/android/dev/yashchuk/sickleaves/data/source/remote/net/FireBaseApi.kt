package com.android.dev.yashchuk.sickleaves.data.source.remote.net

import android.util.Log
import com.android.dev.yashchuk.sickleaves.callbacks.OnUserRegisterListener
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


object FireBaseApi {

    fun addSickLeave(fireStore: FirebaseFirestore) {
        val sickLeave = SickLeave(title = "Title", description = "description")
        val sickLeaveObj = HashMap<String, Any>()
        sickLeaveObj["title"] = sickLeave.title
        sickLeaveObj["description"] = sickLeave.description
        sickLeaveObj["id"] = sickLeave.id
        fireStore.collection("sickleaves")
                .add(sickLeaveObj)
                .addOnSuccessListener { Log.d("FireBaseApi", "Add document" + it.id) }
                .addOnFailureListener{Log.w("FireBaseApi", "Error", it)}
    }

    fun createUser(auth: FirebaseAuth, email: String, password: String, callback: OnUserRegisterListener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            if (it.isSuccessful) {
                callback.onSuccess()
            } else {
                callback.onFailed()
            }
        }
    }
}