package com.example.mymovies.ui.all_movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovies.R
import com.example.mymovies.data.models.MovieItem
import com.example.mymovies.data.models.MovieSearchItem
import com.example.mymovies.data.models.MoviesList
import com.example.mymovies.databinding.SavedItemLayoutBinding
import com.example.mymovies.databinding.SearchItemLayoutBinding
import com.example.mymovies.utils.Utils

class MovieSavedItemAdapter(private val listener : MovieItemListener, private val context: Context) :
    RecyclerView.Adapter<MovieSavedItemAdapter.MovieItemViewHolder>() {

    var movies = ArrayList<MovieItem>()

    inner class MovieItemViewHolder(private val itemBinding : SavedItemLayoutBinding,
                              private val listener : MovieItemListener)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener, View.OnLongClickListener {

        private lateinit var movieItem : MovieItem

        init {
            itemBinding.root.setOnClickListener(this)
            itemBinding.root.setOnLongClickListener(this)
            itemBinding.heart.setOnClickListener{
                Utils.changeHeart(it as ImageView, movieItem.isFav)
                listener.onFavClick(movieItem.id, !movieItem.isFav)
            }
        }

        fun bind(item : MovieItem){
            movieItem = item
            itemBinding.movieTitle.text = item.title
            itemBinding.movieTime.text = Utils.parseMovieLength(item.length, context)
            itemBinding.stars.text = context.getString(R.string.star).repeat(Utils.calculateStars(item.rating))
            Glide.with(itemBinding.root).load(Utils.getHeartPhoto(item.isFav)).override(itemBinding.heart.width, itemBinding.heart.height)
            .into(itemBinding.heart)
            setPosterUri(item.photo)
        }

        private fun setPosterUri(uri : String?){
            if(uri != null){
                Glide.with(itemBinding.root).load(Utils.attachImageURL(uri))
                    .into(itemBinding.backdrop)
            }
            else{
                Glide.with(itemBinding.root).load(context.getDrawable(R.drawable.no_image_placeholder))
                    .into(itemBinding.backdrop)
            }
        }

        override fun onClick(v: View?) {
            if(v != null){
                val location = IntArray(2)
                v.getLocationOnScreen(location)
                location[0] = location[0] + (v.width / 2)
                location[1] = location[1] + (v.height / 2)
                listener.onMovieItemClick(movieItem.id, location,false)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            if(v != null){
                val location = IntArray(2)
                v.getLocationOnScreen(location)
                location[0] = location[0] + (v.width / 2)
                location[1] = location[1] + (v.height / 2)
                listener.onMovieItemClick(movieItem.id, location,true)
            }
            return true
        }
    }

    fun itemAt(position: Int) = movies[position]

    fun setMovies(movies : List<MovieItem>) {
        this.movies.clear()
        this.movies = ArrayList(movies.sortedWith(compareBy{ !it.isFav }))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder =
        MovieItemViewHolder(SavedItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener)

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) = holder.bind(movies[position])

    interface MovieItemListener{
        fun onMovieItemClick(movieId : Int, location : IntArray, longClick : Boolean)

        fun onFavClick(movieId : Int, newStatus : Boolean)
    }
}