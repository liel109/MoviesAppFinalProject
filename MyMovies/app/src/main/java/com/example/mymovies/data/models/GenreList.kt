package com.example.mymovies.data.models

import com.google.gson.annotations.Expose
import java.util.Locale

data class GenreList(
    @Expose
    val genres : List<Genre>)
{

    fun getDict() : Map<Int,String>{
        val genreDictionary = mutableMapOf<Int, String>()
        for(genre in genres){
            genreDictionary[genre.id] = genre.name.lowercase(Locale.ROOT)
        }
        return  genreDictionary
    }
}