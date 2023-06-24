package com.example.mymovies.data.models

import android.graphics.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MoviesList (
    @SerializedName("results")
    @Expose
    val moviesList: ArrayList<MovieSearchItem>) {

    fun addMovies(movies : Collection<MovieSearchItem>){
        moviesList.addAll(movies)
    }
}