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

    override fun deleteSickLeave(id: String, userId: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        localDataSource.deleteSickLeave(id, userId, object : SickLeavesDataSource.DeleteSickLeaveCallback {
            override fun onSickLeaveDeleted() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSickLeaveDeleteFailed() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        remoteDataSource.deleteSickLeave(id, userId, object : SickLeavesDataSource.DeleteSickLeaveCallback {
            override fun onSickLeaveDeleted() {
                callback.onSickLeaveDeleted()
            }

            override fun onSickLeaveDeleteFailed() {
                callback.onSickLeaveDeleteFailed()
            }
        })

        cachedSickLeaves.remove(id)
    }

    override fun deleteAllSickLeaves(callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        localDataSource.deleteAllSickLeaves(object : SickLeavesDataSource.DeleteAllSickLeavesCallback {
            override fun onSickLeavesDeleteFailed() {
                callback.onDataNotAvailable()
                // TODO : if really need return this????
                return
            }
        })

        sickLeaves.forEach { sickLeave ->
            localDataSource.saveSickLeave(userId, sickLeave, object : SickLeavesDataSource.SaveSickLeaveCallback {
                override fun onSickLeaveSaved() {

                }

                override fun onSickLeaveSaveFailed() {
                    callback.onDataNotAvailable()
                }
            })
        }
    }

    private inline fun cacheAndPerform(sickLeave: SickLeave, perform: (SickLeave) -> Unit) {
        val cachedSickLeave = SickLeave(
                id = sickLeave.id,
                title = sickLeave.title,
                description = sickLeave.description,
                startDate = sickLeave.startDate,
                endDate = sickLeave.endDate,
                status = sickLeave.status)
        cachedSickLeaves[cachedSickLeave.id] = cachedSickLeave
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