package com.example.mymovies.ui.edit_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
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
class EditItemViewModel @Inject constructor(val moviesRepository: MoviesRepository) : ViewModel() {
    var fetchedFromRemote = false
    private val _id = MutableLiveData<Int>()

    private val _movieItem = _id.switchMap {
        updateMovieDetails(it)
    }

    val movieItem : LiveData<Resource<MovieItem>> = _movieItem

    fun setId(id : Int){
        _id.value = id
    }

    private fun updateMovieDetails(movieId : Int) : LiveData<Resource<MovieItem>> =
        liveData(Dispatchers.IO) {
            val source = moviesRepository.getMovieFromLocalDB(movieId)
            if(source != null) {
                emit(Resource.success(source))
            }
            else{
                emit(Resource.error(source))
            }

            val newMovieItem = moviesRepository.getMovieFromAPI(movieId)
            fetchedFromRemote = true
            if(newMovieItem.status is Success){
                newMovieItem.status.data!!.isFav = source.isFav
                newMovieItem.status.data.notes = source.notes

                moviesRepository.insertMovie(newMovieItem.status.data)
            }
        }

    fun updateMovieNotes(movieId: Int, newNote : String) {
         viewModelScope.launch{
             moviesRepository.updateMovieNotes(movieId, newNote)
         }
    }
}