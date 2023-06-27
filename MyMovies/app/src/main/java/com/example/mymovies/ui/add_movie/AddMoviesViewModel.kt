package com.example.mymovies.ui.add_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.mymovies.data.models.GenreList
import com.example.mymovies.data.models.MovieItem
import com.example.mymovies.data.models.MoviesList
import com.example.mymovies.data.repos.MoviesRepository
import com.example.mymovies.utils.Resource
import com.example.mymovies.utils.eSearchBy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel(){
    var searchByParam = eSearchBy.Title

    private val _keyword = MutableLiveData<String>()
    private val _moviesList = _keyword.switchMap {
        if(it == ""){
            moviesRepository.getPopularMovies()
        }
        else{
            when(searchByParam){
                eSearchBy.Title -> moviesRepository.getMoviesListByTitle(it)
                eSearchBy.Genre -> {
                    moviesRepository.getMoviesByGenre(it)
                }
            }
        }
    }
    val moviesList : LiveData<Resource<MoviesList>> = _moviesList

    var fetchedGenres = false
    private val _genreString = MutableLiveData<String>()
    private val _genresDict = _genreString.switchMap{ moviesRepository.getGenresDict() }
    val genresDict : LiveData<Resource<GenreList>> = _genresDict

    fun setKeyword(name : String){
        _keyword.value = name
    }

    fun setGenreString(value : String){
        _genreString.value = value
    }

    fun addItem(movieId : Int) : LiveData<Resource<MovieItem>> = moviesRepository.addMovie(movieId)
}