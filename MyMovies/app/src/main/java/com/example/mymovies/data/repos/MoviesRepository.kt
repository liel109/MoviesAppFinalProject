package com.example.mymovies.data.repos

import androidx.lifecycle.LiveData
import com.example.mymovies.data.local.MoviesDao
import com.example.mymovies.data.models.MovieItem
import com.example.mymovies.data.remote.MoviesRemoteDataSource
import com.example.mymovies.utils.DataUtils
import com.example.mymovies.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(private val remoteDataSource: MoviesRemoteDataSource,
                                            private val localDataSource: MoviesDao) {

    fun getMoviesListByTitle(title : String) = DataUtils.fetchRemoteData { remoteDataSource.getMoviesByTitle(title) }

    fun getMoviesByGenre(genre : String) = DataUtils.fetchRemoteData { remoteDataSource.getMoviesByGenres(genre) }

    fun addMovie(id : Int) :LiveData<Resource<MovieItem>> {
        return DataUtils.fetchDataAndSave(
            { localDataSource.getMovie(id) },
            { remoteDataSource.getMovieDetails(id) },
            { localDataSource.addMovie(it) })
    }

    fun getGenresDict() = DataUtils.fetchRemoteData { remoteDataSource.getGenresDict() }

    suspend fun getMovieFromAPI(id : Int) = remoteDataSource.getMovieDetails(id)

    suspend fun getMovieFromLocalDB(id : Int) = localDataSource.getMovieSuspend(id)

    fun getPopularMovies() = DataUtils.fetchRemoteData { remoteDataSource.getPopularMovies() }

    suspend fun insertMovie(movieItem: MovieItem) { localDataSource.addMovie(movieItem) }

    fun getAllMovies() = DataUtils.fetchLocalData { localDataSource.getAllMovies() }

    suspend fun deleteAllSavedMovies() { localDataSource.deleteAll() }

    suspend fun deleteMovie(movieItem: MovieItem) { localDataSource.deleteMovie(movieItem) }

    suspend fun updateMovieFavoriteStatus(id : Int, newStatus : Boolean) { localDataSource.updateMovieFavoriteStatus(id, newStatus) }
    
    suspend fun updateMovieNotes(id : Int, newNote : String){ localDataSource.updateMovieNotes(id, newNote)}
 }