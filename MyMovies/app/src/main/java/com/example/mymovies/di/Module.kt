package com.example.mymovies.di

import android.content.Context
import com.example.mymovies.data.local.AppDatabase
import com.example.mymovies.data.local.MoviesDao
import com.example.mymovies.data.remote.APIService
import com.example.mymovies.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    fun provideGson() : Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson : Gson) : Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    @Provides
    fun provideAPIService(retrofit: Retrofit) : APIService = retrofit.create(APIService::class.java)

    @Provides
    @Singleton
    fun provideLocalDB(@ApplicationContext appContext : Context) : AppDatabase = AppDatabase.getDatabase(appContext)

    @Provides
    @Singleton
    fun provideMoviesDao(database: AppDatabase) : MoviesDao = database.moviesDao()
}