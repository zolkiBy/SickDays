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


    }

    override fun getSickLeave(id: String, callback: SickLeavesDataSource.GetSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveSickLeave(userId: String, sickLeave: SickLeave, callback: SickLeavesDataSource.SaveSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSickLeave(id: String, callback: SickLeavesDataSource.DeleteSickLeaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllSickLeaves(callback: SickLeavesDataSource.DeleteAllSickLeavesCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getSickLeavesFromRemoteDataSource(callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        remoteDataSource.getSickLeaves(, object : SickLeavesDataSource.LoadSickLeavesCallback {
            override fun onSickLeavesLoaded(sickLeaves: List<SickLeave>) {
                refreshCache(sickLeaves)
                refreshLocalDataSource(sickLeaves, object : SickLeavesDataSource.LoadSickLeavesCallback {
                    override fun onSickLeavesLoaded(sickLeaves: List<SickLeave>) {

                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
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

    private fun refreshLocalDataSource(sickLeaves: List<SickLeave>, callback: SickLeavesDataSource.LoadSickLeavesCallback) {
        localDataSource.deleteAllSickLeaves(object : SickLeavesDataSource.DeleteAllSickLeavesCallback {
            override fun onSickLeavesDeleteFailed() {
                callback.onDataNotAvailable()
            }
        })

        sickLeaves.forEach { sickLeave ->
            localDataSource.saveSickLeave("12345", sickLeave, object : SickLeavesDataSource.SaveSickLeaveCallback {
                override fun onSickLeaveSaveFailed() {
                    callback.onDataNotAvailable()
                }
            })
        }
    }

    private inline fun cacheAndPerform(sickLeave: SickLeave, perform: (SickLeave) -> Unit) {
        val cachedSickLeave = SickLeave(sickLeave.title, sickLeave.description)
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