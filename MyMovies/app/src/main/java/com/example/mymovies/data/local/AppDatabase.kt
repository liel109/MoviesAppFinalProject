package com.example.mymovies.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mymovies.data.models.MovieItem

@Database(entities = [MovieItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun moviesDao() : MoviesDao

    companion object{
        @Volatile
        private var instance : AppDatabase? = null

        fun getDatabase(context: Context) = instance ?: synchronized(AppDatabase::class.java) {
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java,"movies")
                .fallbackToDestructiveMigration().build().also { instance = it }
        }
    }
}