package com.example.mymovies.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieSearchItem (
    @Expose
    val id : Int,

    @Expose
    val title : String,

    @SerializedName("release_date")
    @Expose
    val releaseDate : String,

    @SerializedName("genre_ids")
    @Expose
    val genres : List<Int>,

    @SerializedName("poster_path")
    @Expose
    val poster : String?
)