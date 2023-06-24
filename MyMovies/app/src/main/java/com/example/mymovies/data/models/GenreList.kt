package com.example.mymovies.data.models

import com.google.gson.annotations.Expose

data class GenreList(
    @Expose
    val genres : List<Genre>)
{

    fun getDict() : Map<Int,String>{
        val genreDictionary = mutableMapOf<Int, String>()
        for(genre in genres){
            genreDictionary[genre.id] = genre.name
        }
        return  genreDictionary
    }
}