package com.example.mymovies.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class DataUtils {

    companion object {

//        fun <T> fetchData(remoteDbFetch: suspend () -> Resource<T>): LiveData<Resource<T>> =
//            liveData(Dispatchers.IO) {
//                emit(Resource.loading())
//                val source = remoteDbFetch()
//                emit(source)
//            }

        fun <T> fetchRemoteData(remoteDbFetch: suspend () -> Resource<T>): LiveData<Resource<T>> =
            liveData(Dispatchers.IO) {
                emit(Resource.loading())
                val source = remoteDbFetch()
                emit(source)
            }

        fun<T> fetchLocalData(localDbFetch: () -> LiveData<T>) : LiveData<Resource<T>> =
            liveData(Dispatchers.IO) {
                val source = localDbFetch().map{Resource.success(it)}
                emitSource(source)
            }

        fun <T> fetchDataAndSave(localDbFetch: () -> LiveData<T>,
                                 remoteDbFetch : suspend () -> Resource<T>,
                                 localDbSave : suspend (T) -> Unit) : LiveData<Resource<T>> {

            return liveData(Dispatchers.IO) {

                emit(Resource.loading())

                val source = localDbFetch().map { Resource.success(it) }
                emitSource(source)

                val fetchResource = remoteDbFetch()

                if(fetchResource.status is Success){
                    localDbSave(fetchResource.status.data!!)
                    println(fetchResource.status.data!!)
                    emit(fetchResource)
                }
                else if(fetchResource.status is Error){
                    emit(fetchResource)
                    emitSource(source)
                }
            }
        }
    }
}