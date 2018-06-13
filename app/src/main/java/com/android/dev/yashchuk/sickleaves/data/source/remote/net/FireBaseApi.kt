package com.android.dev.yashchuk.sickleaves.data.source.remote.net

import android.util.Log
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

object FireBaseApi {

    val fireStore = FirebaseFirestore.getInstance()

    fun addSickLeave() {
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
}