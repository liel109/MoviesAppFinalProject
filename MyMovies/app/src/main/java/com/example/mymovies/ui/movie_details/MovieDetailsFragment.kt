package com.example.mymovies.ui.movie_details

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.mymovies.R
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

        viewModel.movieItem.observe(viewLifecycleOwner){
            when(it.status){
                is Loading -> {
                    showProgressBar()
                }
                is Success -> {
                    showDetails()
                    if(it.status.data != null) {
                        val movieItem = it.status.data
                        binding.itemTitle.text = movieItem.title
                        binding.itemDesc.text = movieItem.plot
                        binding.itemLength.text = parseMovieLength(movieItem.length)
                        binding.itemComments.text = movieItem.notes
                        binding.itemRating.text = requireContext().getString(R.string.star).repeat(calculateStars(movieItem.rating))
                        binding.itemYear.text = parseReleaseDate(movieItem.year)
                        setPosterUri(movieItem.photo)
                        Glide.with(binding.root).load(getHeartPhoto(movieItem.isFav))
                            .into(binding.heart)
                    }
                }
                is Error -> { println("inside error ${it.status.data}")
                }
            }
        }
        arguments?.getInt("id")?.let {
            viewModel.setId(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun parseReleaseDate(releaseDate : String) : String{
        return releaseDate.substring(0,4)
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

    private fun parseMovieLength(length : Int) : String{
        return "${length/60}${requireContext().getString(R.string.length_hours)} ${length%60}${requireContext().getString(R.string.length_minutes)}"
    }

    private fun calculateStars(rating : Double) : Int{
        return (rating / 2 + 1).toInt()
    }

    private fun getHeartPhoto(isFavorite : Boolean) : Int{
        if(isFavorite){
            return R.drawable.ic_full_heart
        }
        return R.drawable.ic_empty_heart
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
        window?.setBackgroundDrawableResource(R.color.transparent)
        val centerX = resources.displayMetrics.widthPixels / 2
        val centerY = resources.displayMetrics.heightPixels / 2
        val translationX = pressedLocation!![0] - centerX
        val translationY = pressedLocation!![1] - centerY
        val translateAnimatorX = ObjectAnimator.ofFloat(window?.decorView, "translationX", translationX.toFloat() ,0f)
        val translateAnimatorY = ObjectAnimator.ofFloat(window?.decorView, "translationY", translationY.toFloat()-40, 0f)
        val scaleAnimatorX = ObjectAnimator.ofFloat(window?.decorView, "scaleX", 0.54f, 1f)
        val scaleAnimatorY = ObjectAnimator.ofFloat(window?.decorView, "scaleY", 0.3f, 1f)
        val alphaAnimator = ObjectAnimator.ofFloat(window?.decorView, "alpha", 1f, 1f)

        val animatorSet = AnimatorSet().apply {
            playTogether(translateAnimatorX, translateAnimatorY, scaleAnimatorX, scaleAnimatorY, alphaAnimator)
            duration = 400L
        }
        animatorSet.start()
    }

}