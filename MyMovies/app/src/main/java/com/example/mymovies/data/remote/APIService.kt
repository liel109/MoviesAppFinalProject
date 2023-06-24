package com.example.mymovies.data.remote

import com.example.mymovies.data.models.GenreList
import com.example.mymovies.data.models.MovieItem
import com.example.mymovies.data.models.MoviesList
import com.example.mymovies.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("search/movie?")
    suspend fun getAllMoviesByTitle(@Query("query") title: String,
                                    @Query("language") lan : String = Constants.getLocale(),
                                    @Query("api_key") apiKey : String = Constants.AUTHENTICATION_KEY) : Response<MoviesList>

    @GET("discover/movie?")
    suspend fun getMoviesByGenre(@Query("with_genres") genre : String,
                                 @Query("language") lan : String = Constants.getLocale(),
                                 @Query("api_key") apiKey : String = Constants.AUTHENTICATION_KEY) : Response<MoviesList>

    @GET("movie/{id}?")
    suspend fun getMovieDetailsById(@Path("id") id : Int,
                                    @Query("language") lan : String = Constants.getLocale(),
                                    @Query("api_key")  apiKey : String = Constants.AUTHENTICATION_KEY) : Response<MovieItem>

    @GET("movie/popular?")
    suspend fun getPopularMovies(@Query("language") lan : String = Constants.getLocale(),
                                 @Query("api_key")  apiKey : String = Constants.AUTHENTICATION_KEY) : Response<MoviesList>

    @GET("genre/movie/list?")
    suspend fun getGenresDict(@Query("language") lan : String = Constants.getLocale(),
                              @Query("api_key") apiKey : String = Constants.AUTHENTICATION_KEY) : Response<GenreList>
}