package com.android.dev.yashchuk.sickleaves.data.source.remote

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class SickLeavesRemoteDataSource: SickLeavesDataSource {

    private val id = "id"
    private val title = "title"
    private val description = "description"


    override fun getSickLeaves(userId: String, callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        FirebaseFirestore.getInstance().collection(userId)
                .get()
                .addOnCompleteListener { task: Task<QuerySnapshot> ->
                    if (task.isSuccessful) {

                    }
                }
    }

    override fun getSickLeave(id: String, callback: SickLeavesDataSource.GetSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveSickLeave(userId: String, sickLeave: SickLeave, callback: SickLeavesDataSource.SaveSickLeaveCallback) {
        /*val sickLeaveMap = HashMap<String, Any>()
        sickLeaveMap[id] = sickLeave.id
        sickLeaveMap[title] = sickLeave.title
        sickLeaveMap[description] = sickLeave.description*/
        FirebaseFirestore.getInstance().collection(userId).document().set(sickLeave)
                /*.add(sickLeaveMap)*/
                .addOnSuccessListener { callback.onSickLeaveSaved() }
                .addOnFailureListener { callback.onSickLeaveSaveFailed() }

    }

    override fun deleteSickLeave(id: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllSickLeaves(callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
