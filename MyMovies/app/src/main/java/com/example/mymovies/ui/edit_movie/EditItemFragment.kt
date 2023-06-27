package com.example.mymovies.ui.edit_movie

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mymovies.R
import com.example.mymovies.data.models.MovieItem
import com.example.mymovies.databinding.EditItemFragmentBinding
import com.example.mymovies.utils.Constants
import com.example.mymovies.utils.Error
import com.example.mymovies.utils.Loading
import com.example.mymovies.utils.Success
import com.example.mymovies.utils.Utils
import com.example.mymovies.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.Util

@AndroidEntryPoint
class EditItemFragment : Fragment() {

    private val viewModel : EditItemViewModel by viewModels()
    private var binding : EditItemFragmentBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditItemFragmentBinding.inflate(inflater, container, false)

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
        arguments?.getInt("id")?.let {
            viewModel.setId(it)
        }
    }

    private fun setDetailsInView(movieItem : MovieItem){
        binding.itemTitle.text = movieItem.title
        binding.itemDesc.text = movieItem.plot
        binding.itemLength.text = Utils.parseMovieLength(movieItem.length, requireContext())
        binding.notesEditText.setText(movieItem.notes)
        binding.itemRating.text = requireContext().getString(R.string.star).repeat(Utils.calculateStars(movieItem.rating))
        binding.itemYear.text = Utils.parseReleaseDate(movieItem.year)
        setPosterUri(movieItem.photo)
        Glide.with(binding.root).load(Utils.getHeartPhoto(movieItem.isFav))
            .into(binding.heart)

        binding.doneBtn.setOnClickListener{
            if(viewModel.fetchedFromRemote){
                viewModel.updateMovieNotes(movieItem.id, binding.notesEditText.text.toString())
                findNavController().navigate(R.id.action_editItemFragment_to_allMoviesFragment)
            }
            else{
                Toast.makeText(context,"Can't update yet!",Toast.LENGTH_SHORT).show()
            }
        }

        binding.heart.setOnClickListener{
            if(viewModel.fetchedFromRemote) {
                Utils.changeHeart(it as ImageView, movieItem.isFav)
                viewModel.setFavorite(movieItem.id, !movieItem.isFav)
            }
            else{
                Toast.makeText(context,"Can't update yet!",Toast.LENGTH_SHORT).show()
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

        val window = requireActivity().window
        val centerX = resources.displayMetrics.widthPixels
        val centerY = resources.displayMetrics.heightPixels
        if(pressedLocation != null){
            Utils.scaleInAnimation(window, pressedLocation, intArrayOf(centerX,centerY))
        }
    }
}