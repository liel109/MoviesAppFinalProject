package com.example.mymovies.ui.all_movies

import android.app.AlertDialog
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.utils.widget.MockView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.mymovies.R
import com.example.mymovies.data.models.MovieItem
import com.example.mymovies.databinding.AllMoviesFragmentBinding
import com.example.mymovies.utils.Error
import com.example.mymovies.utils.Loading
import com.example.mymovies.utils.Success
import com.example.mymovies.utils.Utils
import com.example.mymovies.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllMoviesFragment : Fragment(), MovieSavedItemAdapter.MovieItemListener {

    private var binding: AllMoviesFragmentBinding by autoCleared()
    private val viewModel : AllMoviesViewModel by viewModels()
    private lateinit var adapter: MovieSavedItemAdapter
    private var updateRequired = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllMoviesFragmentBinding.inflate(inflater, container, false)

        updateRequired = true
        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_allMoviesFragment_to_addMovieFragment)
        }

        binding.clearButton.setOnClickListener{
            if(adapter.itemCount != 0) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(getString(R.string.confirmation))
                builder.setMessage(getString(R.string.delete_all_confirmation_dialog))

                builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deleteAllMovies()
                }
                builder.setNegativeButton(getString(R.string.no), null)

                builder.create().show()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MovieSavedItemAdapter(this, requireContext())
        binding.recycleView.layoutManager = if(requireContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayoutManager(requireContext())
        }
        else{
            GridLayoutManager(requireContext(),2)
        }
        binding.recycleView.adapter = adapter

        viewModel.savedMovies.observe(viewLifecycleOwner){
            if(it!=null){
                when(it.status){
                    is Loading -> { binding.progressBar.visibility = View.VISIBLE }
                    is Success -> {
                        if(it.status.data != null) {
                            adapter.setMovies(it.status.data)
                            if(updateRequired) {
                                viewModel.updateMovies(it.status.data)
                                updateRequired = false
                                binding.progressBar.visibility = View.GONE
                            }
                        }
                    }
                    is Error -> {
                        Utils.showNoConnectionToast(requireContext())
                    }
                }
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(getString(R.string.confirmation))
                builder.setMessage(getString(R.string.delete_confirmation_dialog))
                builder.setCancelable(false)

                builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deleteMovie(adapter.itemAt(viewHolder.adapterPosition))
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                }
                builder.setNegativeButton(getString(R.string.no)) {_,_ ->
                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                }

                builder.create().show()
            }
        }).attachToRecyclerView(binding.recycleView)

        binding.recycleView.overScrollMode = RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS
    }

    override fun onMovieItemClick(movieId: Int, location : IntArray, longClick : Boolean) {
        if(!longClick){
            findNavController().navigate(R.id.action_allMoviesFragment_to_movieDetailsFragment, bundleOf("id" to movieId, "location" to location))
        }
        else{
            findNavController().navigate(R.id.action_allMoviesFragment_to_editItemFragment, bundleOf("id" to movieId, "location" to location))
        }
    }

    override fun onFavClick(movieId: Int, newStatus: Boolean) {
        viewModel.setFavorite(movieId, newStatus)
    }
}