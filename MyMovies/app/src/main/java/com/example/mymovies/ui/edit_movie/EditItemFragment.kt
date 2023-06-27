package com.example.mymovies.ui.edit_movie

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mymovies.R
import com.example.mymovies.databinding.EditItemFragmentBinding
import com.example.mymovies.utils.Constants
import com.example.mymovies.utils.Error
import com.example.mymovies.utils.Loading
import com.example.mymovies.utils.Success
import com.example.mymovies.utils.Utils
import com.example.mymovies.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

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
                        val movieItem = it.status.data
                        binding.itemTitle.text = movieItem.title
                        binding.itemDesc.text = movieItem.plot
                        binding.itemLength.text = parseMovieLength(movieItem.length)
                        binding.notesEditText.setText(movieItem.notes)
                        binding.itemRating.text = requireContext().getString(R.string.star).repeat(calculateStars(movieItem.rating))
                        binding.itemYear.text = parseReleaseDate(movieItem.year)
                        setPosterUri(movieItem.photo)
                        Glide.with(binding.root).load(getHeartPhoto(movieItem.isFav))
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
                    }
                }
                is Error -> { println("inside error ${it.status.data}")
                }
            }
        }
        arguments?.getInt("id")?.let {
            viewModel.setId(it)
        }

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

        val window = requireActivity().window
        val centerX = resources.displayMetrics.widthPixels / 2
        val centerY = resources.displayMetrics.heightPixels / 2
        val translationX = pressedLocation!![0] - centerX
        val translationY = pressedLocation!![1] - centerY
        val translateAnimatorX = ObjectAnimator.ofFloat(window?.decorView, "translationX", translationX.toFloat() ,0f)
        val translateAnimatorY = ObjectAnimator.ofFloat(window?.decorView, "translationY", translationY.toFloat(), 0f)
        val scaleAnimatorX = ObjectAnimator.ofFloat(window?.decorView, "scaleX", 0.7f, 1f)
        val scaleAnimatorY = ObjectAnimator.ofFloat(window?.decorView, "scaleY", 0.3f, 1f)
        val alphaAnimator = ObjectAnimator.ofFloat(window?.decorView, "alpha", 1f, 1f)

        val animatorSet = AnimatorSet().apply {
            playTogether(translateAnimatorX, translateAnimatorY, scaleAnimatorX, scaleAnimatorY, alphaAnimator)
            duration = Constants.POP_ANIMATION_DURATION
        }
        animatorSet.start()
    }
}