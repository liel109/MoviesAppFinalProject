package com.example.mymovies.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mymovies.data.models.MovieItem

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movieItem: MovieItem)

    @Delete
    suspend fun deleteMovie(movieItem: MovieItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMovie(movieItem: MovieItem)

    @Query("UPDATE 'movies' SET 'favorite' = :newStatus WHERE id = :id")
    suspend fun updateMovieFavoriteStatus(id : Int, newStatus : Boolean)

    @Query("UPDATE 'movies' SET 'notes' = :newNote WHERE id = :id")
    suspend fun updateMovieNotes(id : Int, newNote : String)

    @Query("SELECT * FROM 'movies' ORDER BY 'favorite' DESC")
    fun getAllMovies() : LiveData<List<MovieItem>>

    @Query("SELECT * FROM 'movies' WHERE id=:id")
    suspend fun getMovieSuspend(id : Int) : MovieItem

    @Query("SELECT * FROM 'movies' WHERE id=:id")
    fun getMovie(id : Int) : LiveData<MovieItem>

    @Query("DELETE FROM 'movies'")
    suspend fun deleteAll()
}