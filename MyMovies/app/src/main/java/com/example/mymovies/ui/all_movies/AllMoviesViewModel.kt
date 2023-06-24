package com.example.mymovies.ui.all_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovies.data.models.MovieItem
import com.example.mymovies.data.repos.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllMoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {
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
}