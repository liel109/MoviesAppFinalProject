package com.example.mymovies.utils

import java.util.Locale

class Constants {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "http://image.tmdb.org/t/p/w500"
        const val AUTHENTICATION_KEY = "17fc2b15219e36d398f4b041aa08856f"
        lateinit var ID_TO_STRING_GENRES : Map<Int, String>

        lateinit var STRING_TO_ID_GENRES : Map<String, Int>

        fun getLocale() : String{
            if(Locale.getDefault().language == "iw") {
                return "he-IL"
            }
            return "en-US"
        }

        fun setGenresDict(genresDict : Map<Int, String>){
            ID_TO_STRING_GENRES = genresDict
            STRING_TO_ID_GENRES = ID_TO_STRING_GENRES.entries.associate { (key, value) -> value to key }
        }
    }
}