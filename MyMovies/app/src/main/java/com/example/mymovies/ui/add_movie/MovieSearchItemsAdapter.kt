package com.example.mymovies.ui.add_movie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovies.R
import com.example.mymovies.data.models.MovieSearchItem
import com.example.mymovies.data.models.MoviesList
import com.example.mymovies.databinding.SearchItemLayoutBinding
import com.example.mymovies.utils.Constants
import com.example.mymovies.utils.Utils
import java.lang.StringBuilder
import java.util.Locale

class MovieSearchItemsAdapter(private val listener : MovieSearchItemListener, private val context: Context)  : RecyclerView.Adapter<MovieSearchItemsAdapter.MoviesViewHolder>(){

    private var movies = MoviesList(ArrayList())

    inner class MoviesViewHolder(private val itemBinding : SearchItemLayoutBinding,
        private val listener: MovieSearchItemListener) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {
        private lateinit var movieItem : MovieSearchItem

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(movieItem: MovieSearchItem, genreRequested : String? = null) {
            this.movieItem = movieItem
            itemBinding.title.text = movieItem.title
            itemBinding.genres.text = if(movieItem.genres.isNotEmpty()){
                parseGenres(movieItem.genres, genreRequested)
            }
            else{
                ""
            }
            itemBinding.releaseDate.text = if(movieItem.releaseDate.isNotEmpty()) {
                parseReleaseDate(movieItem.releaseDate)
            }
            else{
                context.getString(R.string.tbd)
            }
            setPosterUri(movieItem.poster)
        }

        override fun onClick(v: View?) {
            Utils.pressedAnimation(v!!)
            listener.onMovieClick(movieItem.id)
        }

        private fun setPosterUri(uri : String?){
            if(uri != null){
                Glide.with(itemBinding.root).load(Utils.attachImageURL(uri))
                    .into(itemBinding.poster)
            }
            else{
                Glide.with(itemBinding.root).load(AppCompatResources.getDrawable(context, R.drawable.no_image_placeholder))
                    .into(itemBinding.poster)
            }
        }

        private fun parseGenres(genres : List<Int>, genreRequested : String?) : String {
            val stringBuilder = StringBuilder()
            var i = 0
            var numOfGenresToAdd = 3
            var mutableGenreRequested = genreRequested

            if(mutableGenreRequested != null){
                mutableGenreRequested = parseGenre(mutableGenreRequested)
                stringBuilder.append("${mutableGenreRequested.lowercase(Locale.getDefault())} | ")
                numOfGenresToAdd--
            }

            while(i < numOfGenresToAdd && i < genres.size){
                val genre = Constants.ID_TO_STRING_GENRES[genres[i]]
                if(genre != mutableGenreRequested){
                    stringBuilder.append("$genre | ")
                    i++
                }
            }
            stringBuilder.delete(stringBuilder.length-3,stringBuilder.length)
            return stringBuilder.toString()
        }

        private fun parseGenre(genreToAdd : String) : String {
            return genreToAdd.lowercase(Locale.ENGLISH).replaceFirstChar { it.uppercase() }
        }

        private fun parseReleaseDate(releaseDate : String) : String{
            return releaseDate.substring(0,4)
        }
    }

    fun setMovies(movies : MoviesList){
        this.movies.moviesList.clear()
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(movies.moviesList[position])
    }

    override fun getItemCount(): Int {
        return movies.moviesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = SearchItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MoviesViewHolder(binding, listener)
    }

    interface MovieSearchItemListener{
        fun onMovieClick(movieId : Int)
    }
}
