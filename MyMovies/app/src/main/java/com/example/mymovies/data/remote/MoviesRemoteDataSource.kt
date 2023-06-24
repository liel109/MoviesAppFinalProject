package com.example.mymovies.data.remote

import android.util.Log
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRemoteDataSource @Inject constructor(private val apiService: APIService) : BaseDataSource(){

    suspend fun getMoviesByTitle(title : String) = getResult { apiService.getAllMoviesByTitle(title) }

    suspend fun getMoviesByGenres(genre : String) = getResult { apiService.getMoviesByGenre(genre) }

    suspend fun getMovieDetails(id : Int) = getResult {  apiService.getMovieDetailsById(id) }

    suspend fun getPopularMovies() = getResult { apiService.getPopularMovies() }

    suspend fun getGenresDict() = getResult { apiService.getGenresDict() }
}