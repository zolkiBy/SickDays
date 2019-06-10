package com.android.dev.yashchuk.sickleaves.data.source.remote

import android.support.annotation.VisibleForTesting
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.source.SickLeavesDataSource
import com.google.firebase.firestore.FirebaseFirestore

class SickLeavesRemoteDataSource : SickLeavesDataSource {

    override fun getSickLeaves(userId: String, callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        val sickLeaves = mutableListOf<SickLeave>()
        FirebaseFirestore.getInstance().collection(userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            sickLeaves.add(document.toObject(SickLeave::class.java))
                        }

                        callback.onSickLeavesLoaded(sickLeaves)
                    } else {
                        callback.onDataNotAvailable()
                    }
                }
    }

    override fun getSickLeave(userId: String, sickLeaveId: String, callback: SickLeavesDataSource.GetSickLeaveCallback) {
        FirebaseFirestore.getInstance().collection(sickLeaveId)
                .document(sickLeaveId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null) {
                            if (document.exists()) {
                                callback.onSickLeaveLoaded(document.toObject(SickLeave::class.java)!!)
                            }
                        }
                    }
                }
    }

    override fun saveSickLeave(userId: String, sickLeave: SickLeave, callback: SickLeavesDataSource.SaveSickLeaveCallback) {
        FirebaseFirestore.getInstance().collection(userId).document(sickLeave.id.toString()).set(sickLeave)
                .addOnSuccessListener { callback.onSickLeaveSaved() }
                .addOnFailureListener { callback.onSickLeaveSaveFailed() }

    }

    override fun deleteSickLeave(userId: String, sickLeaveId: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        FirebaseFirestore.getInstance().collection(userId)
                .document(sickLeaveId)
                .delete()
                .addOnSuccessListener { callback.onSickLeaveDeleted() }
                .addOnFailureListener { callback.onSickLeaveDeleteFailed() }
    }

    override fun deleteAllSickLeaves(userId: String, sickLeaves: List<SickLeave>, callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        val sickLeaveIds = sickLeaves.map { it.id }
        val lastId = sickLeaveIds.last()
        for (sickLeaveId in sickLeaveIds) {
            FirebaseFirestore.getInstance().collection(userId)
                    .document(sickLeaveId.toString())
                    .delete()
                    .addOnSuccessListener {
                        if (sickLeaveId == lastId) {
                            callback.onSickLeavesDeleted()
                        }
                    }
                    .addOnFailureListener { callback.onSickLeavesDeleteFailed() }
        }
    }

    override fun refreshSickLeaves() {
        // implement by SickLeaveRepository
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
