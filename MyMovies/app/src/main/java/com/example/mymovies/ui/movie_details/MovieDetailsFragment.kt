package com.example.mymovies.ui.movie_details

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mymovies.R
import com.example.mymovies.data.models.MovieItem
import com.example.mymovies.databinding.MovieDetailsFragmentBinding
import com.example.mymovies.utils.Constants
import com.example.mymovies.utils.Error
import com.example.mymovies.utils.Loading
import com.example.mymovies.utils.Success
import com.example.mymovies.utils.Utils
import com.example.mymovies.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder
import java.util.Locale

@AndroidEntryPoint
class MovieDetailsFragment : DialogFragment() {

    private val viewModel : MovieDetailsViewModel by viewModels()
    private var binding : MovieDetailsFragmentBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        binding.itemCard.minimumWidth = (resources.displayMetrics.widthPixels * 0.85).toInt()
        binding.itemCard.minimumHeight = (resources.displayMetrics.heightPixels * 0.85).toInt()

        arguments?.getInt("id")?.let {
            viewModel.setId(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.movieItem.observe(viewLifecycleOwner){
            when(it.status){
                is Loading -> {
                    showProgressBar()
                }
                is Success -> {
                    showDetails()
                    if(it.status.data != null) {
                        setDetailsInView(it.status.data)
                    }
                }
                is Error -> { Utils.showNoConnectionToast(requireContext()) }
            }
        }
    }

    private fun setPosterUri(uri : String?){
        if(uri != null){
            Glide.with(binding.root).load(Utils.attachImageURL(uri))
                .into(binding.itemImage)
        }
        else{
            Glide.with(binding.root).load(requireContext().getDrawable(R.drawable.no_image_placeholder))
                .into(binding.itemImage)
        }
    }

    private fun setDetailsInView(movieItem : MovieItem){
        binding.itemTitle.text = movieItem.title
        binding.itemDesc.text = movieItem.plot
        binding.itemLength.text = Utils.parseMovieLength(movieItem.length, requireContext())
        binding.itemComments.text = (movieItem.notes)
        binding.itemRating.text = requireContext().getString(R.string.star).repeat(Utils.calculateStars(movieItem.rating))
        binding.itemYear.text = Utils.parseReleaseDate(movieItem.year)
        setPosterUri(movieItem.photo)
        Glide.with(binding.root).load(Utils.getHeartPhoto(movieItem.isFav))
            .into(binding.heart)
    }

    private fun showDetails(){
        binding.detailsScrollView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.detailsScrollView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onStart(){
        super.onStart()
        val pressedLocation = arguments?.getIntArray("location")

        val window = dialog?.window
        val layoutParams = window?.attributes
        layoutParams?.width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        layoutParams?.height = (resources.displayMetrics.heightPixels * 0.85).toInt()
        window?.setBackgroundDrawableResource(R.color.transparent)
        val centerX = resources.displayMetrics.widthPixels
        val centerY = resources.displayMetrics.heightPixels
        if(window != null && pressedLocation != null) {
            Utils.scaleInAnimation(window, pressedLocation, intArrayOf(centerX, centerY))
        }
    }

}