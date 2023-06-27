package com.example.mymovies.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers

class DataUtils {

    companion object {
        fun <T> fetchRemoteData(remoteDbFetch: suspend () -> Resource<T>): LiveData<Resource<T>> =
            liveData(Dispatchers.IO) {
                emit(Resource.loading())
                val source = remoteDbFetch()
                emit(source)
            }

        fun <T> fetchLocalData(localDbFetch: () -> LiveData<T>): LiveData<Resource<T>> =
            liveData(Dispatchers.IO) {
                val source = localDbFetch().map { Resource.success(it) }
                emitSource(source)
            }

        fun <T> fetchDataAndSave(
            localDbFetch: () -> LiveData<T>,
            remoteDbFetch: suspend () -> Resource<T>,
            localDbSave: suspend (T) -> Unit
        ): LiveData<Resource<T>> =

            liveData(Dispatchers.IO) {

                emit(Resource.loading())
                val source = localDbFetch().map { Resource.success(it) }
                emitSource(source)

                val fetchResource = remoteDbFetch()

                if (fetchResource.status is Success) {
                    localDbSave(fetchResource.status.data!!)
                    emit(fetchResource)
                } else if (fetchResource.status is Error) {
                    println("Error")
                    emit(fetchResource)
                    emitSource(source)
                }
            }
    }
}