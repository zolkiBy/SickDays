package com.android.dev.yashchuk.sickleaves.data.source

import com.android.dev.yashchuk.sickleaves.data.SickLeave

class SickLeavesRepository(
        val localDataSource: SickLeavesDataSource,
        val remoteDataSource: SickLeavesDataSource
) : SickLeavesDataSource {

    var cachedSickLeaves: LinkedHashMap<String, SickLeave> = LinkedHashMap()

    var cacheIsDirty = false

    override fun getSickLeaves(userId: String, callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        if (cachedSickLeaves.isNotEmpty() && !cacheIsDirty) {
            callback.onSickLeavesLoaded(ArrayList(cachedSickLeaves.values))
            return
        }

        if (cacheIsDirty) {
            getSickLeavesFromRemoteDataSource(userId, callback)
        } else {
            localDataSource.getSickLeaves(userId, object : SickLeavesDataSource.LoadSickLeavesCallback {
                override fun onSickLeavesLoaded(sickLeaves: List<SickLeave>) {
                    refreshCache(sickLeaves)
                    callback.onSickLeavesLoaded(ArrayList(cachedSickLeaves.values))
                }

                override fun onDataNotAvailable() {
                    getSickLeavesFromRemoteDataSource(userId, callback)
                }
            })
        }
    }

    override fun getSickLeave(userId: String, sickLeaveId: String, callback: SickLeavesDataSource.GetSickLeaveCallback) {
        val cachedSickLeave = getCachedSickLeaveWithId(sickLeaveId)

        if (cachedSickLeave != null) {
            callback.onSickLeaveLoaded(cachedSickLeave)
            return
        }

        localDataSource.getSickLeave(userId, sickLeaveId, object : SickLeavesDataSource.GetSickLeaveCallback {
            override fun onSickLeaveLoaded(sickLeave: SickLeave) {
                cacheAndPerform(sickLeave) {
                    callback.onSickLeaveLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                remoteDataSource.getSickLeave(userId, sickLeaveId, object : SickLeavesDataSource.GetSickLeaveCallback {
                    override fun onSickLeaveLoaded(sickLeave: SickLeave) {
                        cacheAndPerform(sickLeave) {
                            callback.onSickLeaveLoaded(it)
                        }
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }
        })
    }

    override fun saveSickLeave(userId: String, sickLeave: SickLeave, callback: SickLeavesDataSource.SaveSickLeaveCallback) {
        cacheAndPerform(sickLeave) {
            localDataSource.saveSickLeave(userId, it, callback)
            remoteDataSource.saveSickLeave(userId, it, callback)
        }
    }

    override fun deleteSickLeave(userId: String, sickLeaveId: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        remoteDataSource.deleteSickLeave(userId, sickLeaveId, object : SickLeavesDataSource.DeleteSickLeaveCallback {
            override fun onSickLeaveDeleted() {
                localDataSource.deleteSickLeave(userId, sickLeaveId, object : SickLeavesDataSource.DeleteSickLeaveCallback {
                    override fun onSickLeaveDeleted() {
                        cachedSickLeaves.remove(sickLeaveId)
                        callback.onSickLeaveDeleted()
                    }

                    override fun onSickLeaveDeleteFailed() {
                        callback.onSickLeaveDeleteFailed()
                        refreshSickLeaves()
                    }
                })
            }

            override fun onSickLeaveDeleteFailed() {
                callback.onSickLeaveDeleteFailed()
                refreshSickLeaves()
            }
        })

    }

    override fun deleteAllSickLeaves(
            userId: String,
            sickLeaves: List<SickLeave>,
            callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        remoteDataSource.deleteAllSickLeaves(
                userId,
                sickLeaves,
                object : SickLeavesDataSource.DeleteAllSickLeavesCallback {
                    override fun onSickLeavesDeleted() {
                        localDataSource.deleteAllSickLeaves(
                                userId,
                                sickLeaves,
                                object : SickLeavesDataSource.DeleteAllSickLeavesCallback {
                                    override fun onSickLeavesDeleted() {
                                        cachedSickLeaves.clear()
                                        callback.onSickLeavesDeleted()
                                    }

                                    override fun onSickLeavesDeleteFailed() {
                                        callback.onSickLeavesDeleteFailed()
                                        refreshSickLeaves()
                                    }
                                })
                    }

                    override fun onSickLeavesDeleteFailed() {
                        callback.onSickLeavesDeleteFailed()
                        refreshSickLeaves()
                    }
                })
    }

    override fun refreshSickLeaves() {
        cacheIsDirty = true
    }

    private fun getCachedSickLeaveWithId(id: String): SickLeave? {
        return cachedSickLeaves[id]
    }

    private fun getSickLeavesFromRemoteDataSource(userId: String, callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        remoteDataSource.getSickLeaves(userId, object : SickLeavesDataSource.LoadSickLeavesCallback {
            override fun onSickLeavesLoaded(sickLeaves: List<SickLeave>) {
                refreshCache(sickLeaves)
                refreshLocalDataSource(userId, sickLeaves, callback)
                callback.onSickLeavesLoaded(sickLeaves)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(sickLeaves: List<SickLeave>) {
        cachedSickLeaves.clear()
        sickLeaves.forEach { sickLeave ->
            cacheAndPerform(sickLeave) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(userId: String, sickLeaves: List<SickLeave>, callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        localDataSource.deleteAllSickLeaves(
                userId,
                sickLeaves,
                object : SickLeavesDataSource.DeleteAllSickLeavesCallback {
                    override fun onSickLeavesDeleted() {
                        sickLeaves.forEach { sickLeave ->
                            localDataSource.saveSickLeave(
                                    userId,
                                    sickLeave,
                                    object : SickLeavesDataSource.SaveSickLeaveCallback {
                                        override fun onSickLeaveSaved() {
                                            // do nothing, sick leave saved
                                        }

                                        override fun onSickLeaveSaveFailed() {
                                            callback.onDataNotAvailable()
                                        }
                                    })
                        }
                    }

                    override fun onSickLeavesDeleteFailed() {
                        callback.onDataNotAvailable()
                    }
                })
    }

    private inline fun cacheAndPerform(sickLeave: SickLeave, perform: (SickLeave) -> Unit) {
        val cachedSickLeave = SickLeave(
                id = sickLeave.id,
                title = sickLeave.title,
                description = sickLeave.description,
                startDate = sickLeave.startDate,
                endDate = sickLeave.endDate,
                status = sickLeave.status)
        cachedSickLeaves[cachedSickLeave.id.toString()] = cachedSickLeave
        perform(cachedSickLeave)
    }

    companion object {
        private var INSTANCE: SickLeavesRepository? = null

        @JvmStatic
        fun getInstance(localDataSource: SickLeavesDataSource,
                        remoteDataSource: SickLeavesDataSource)
                : SickLeavesRepository {
            INSTANCE ?: synchronized(SickLeavesRepository::class.java) {
                INSTANCE ?: SickLeavesRepository(localDataSource, remoteDataSource)
                        .also { sickLeavesRepository ->
                            INSTANCE = sickLeavesRepository
                        }
            }

            return INSTANCE!!
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}