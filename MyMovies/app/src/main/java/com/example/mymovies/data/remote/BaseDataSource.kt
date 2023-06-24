package com.example.mymovies.data.remote

import android.util.Log
import com.example.mymovies.utils.Resource
import retrofit2.Response
import java.lang.Exception

abstract class BaseDataSource {
    protected suspend fun<T> getResult(call : suspend () -> Response<T>) : Resource<T> {
        try {
            val result = call()
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) {
                    return Resource.success(body)
                }
            }
            return Resource.error("Failed Fetching Data - ${result.message()} ${result.code()}")
        }
        catch (e: Exception){
            Log.d("Inside Catch", "${e.localizedMessage}")
            return Resource.error("Failed Fetching Data - ${e.localizedMessage ?: e.toString()}")
        }
    }

}