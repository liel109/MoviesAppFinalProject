package com.example.mymovies.ui.all_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.mymovies.data.models.MovieItem
import com.example.mymovies.data.repos.MoviesRepository
import com.example.mymovies.utils.Resource
import com.example.mymovies.utils.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllMoviesViewModel @Inject constructor(val moviesRepository: MoviesRepository) : ViewModel() {
    val savedMovies = moviesRepository.getAllMovies()

    fun deleteAllMovies() {
        viewModelScope.launch {
            moviesRepository.deleteAllSavedMovies()
        }
    }

    fun deleteMovie(movieItem : MovieItem) {
        viewModelScope.launch {
            moviesRepository.deleteMovie(movieItem)
        }
    }

    fun setFavorite(movieId : Int, newStatus : Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            Thread.sleep(125)
            moviesRepository.updateMovieFavoriteStatus(movieId, newStatus)
        }
    }

    fun updateMovies(savedMoviesList : List<MovieItem>){
        viewModelScope.launch(Dispatchers.IO) {
            for (movie in savedMoviesList) {
                val updatedMovie = moviesRepository.getMovieFromAPI(movie.id)
                if(updatedMovie.status is Success){
                    updatedMovie.status.data!!.isFav = movie.isFav
                    updatedMovie.status.data.notes = movie.notes

                    moviesRepository.insertMovie(updatedMovie.status.data)
                }
            }
        }
    }
}