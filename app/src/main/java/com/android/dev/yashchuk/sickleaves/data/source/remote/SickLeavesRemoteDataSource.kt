package com.android.dev.yashchuk.sickleaves.data.source.remote

import android.support.annotation.VisibleForTesting
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.android.dev.yashchuk.sickleaves.data.source.local.SickLeavesDao
import com.android.dev.yashchuk.sickleaves.data.source.local.SickLeavesLocalDataSource
import com.android.dev.yashchuk.sickleaves.utils.AppExecutors
import com.google.firebase.firestore.FirebaseFirestore

class SickLeavesRemoteDataSource : SickLeavesDataSource {

    private val id = "id"
    private val title = "title"
    private val description = "description"


    override fun getSickLeaves(userId: String, callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        val sickLeaves = mutableListOf<SickLeave>()
        FirebaseFirestore.getInstance().collection(userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            sickLeaves.add(document.toObject(SickLeave::class.java))
                        }

                        callback.onSickLeavesLoaded(sickLeaves)
                    } else {
                        callback.onDataNotAvailable()
                    }
                }
    }

    override fun getSickLeave(id: String, callback: SickLeavesDataSource.GetSickLeaveCallback) {
        FirebaseFirestore.getInstance().collection(id)
                .document(id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document.exists()) {
                            callback.onSickLeaveLoaded(document.toObject(SickLeave::class.java)!!)
                        }
                    }
                }
    }

    override fun saveSickLeave(userId: String, sickLeave: SickLeave, callback: SickLeavesDataSource.SaveSickLeaveCallback) {
        /*val sickLeaveMap = HashMap<String, Any>()
        sickLeaveMap[id] = sickLeave.id
        sickLeaveMap[title] = sickLeave.title
        sickLeaveMap[description] = sickLeave.description*/
        FirebaseFirestore.getInstance().collection(userId).document(sickLeave.id).set(sickLeave)
                /*.add(sickLeaveMap)*/
                .addOnSuccessListener { callback.onSickLeaveSaved() }
                .addOnFailureListener { callback.onSickLeaveSaveFailed() }

    }

    override fun deleteSickLeave(id: String, userId: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        FirebaseFirestore.getInstance().collection(userId)
                .document(id)
                .delete()
                .addOnSuccessListener {
                    callback.onSickLeaveDeleted()
                }
                .addOnFailureListener {
                    callback.onSickLeaveDeleteFailed()
                }
    }

    override fun deleteAllSickLeaves(callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshSickLeaves() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private var INSTANCE: SickLeavesRemoteDataSource? = null

        @JvmStatic
        fun getInstance()
                : SickLeavesRemoteDataSource {
            INSTANCE ?: synchronized(SickLeavesRemoteDataSource::class.java) {
                INSTANCE ?: SickLeavesRemoteDataSource()
                        .also { sickLeavesRemoteDataSource ->
                            INSTANCE = sickLeavesRemoteDataSource
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
