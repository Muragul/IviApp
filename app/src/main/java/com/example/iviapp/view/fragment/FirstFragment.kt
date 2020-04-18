package com.example.iviapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.iviapp.*
import com.example.iviapp.view.adapter.MoviesAdapter
import com.example.iviapp.model.movie.Movie
import com.example.iviapp.view_model.MovieListViewModel
import com.example.iviapp.view_model.ViewModelProviderFactory
import kotlin.collections.ArrayList

class FirstFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MoviesAdapter
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var movieList: List<Movie>
    private lateinit var progressBar: ProgressBar
    private lateinit var movieListViewModel: MovieListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater
            .inflate(
                R.layout.activity_second,
                container, false
            ) as ViewGroup

        progressBar = rootView.findViewById(R.id.progressBar)
        recyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        swipeContainer = rootView.findViewById(R.id.main_content)
        val toolbar: TextView = rootView.findViewById(R.id.toolbar)
        toolbar.text = "Popular"

        val viewModelProviderFactory = ViewModelProviderFactory(activity as Context)
        movieListViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(MovieListViewModel::class.java)

        swipeContainer.setOnRefreshListener {
            initViews()
            movieListViewModel.getMovies()
        }

        movieListViewModel.getMovies()
        movieListViewModel.getFavorites()
        movieListViewModel.liveData.observe(this, Observer { result ->
            when (result) {
                is MovieListViewModel.State.ShowLoading -> {
                    swipeContainer.isRefreshing = true
                }
                is MovieListViewModel.State.HideLoading -> {
                    swipeContainer.isRefreshing = false
                }
                is MovieListViewModel.State.Result -> {
                    adapter.movieList = result.list
                    adapter.notifyDataSetChanged()
                }
            }
        })
        initViews()
        return rootView
    }

    private fun initViews() {
        movieList = ArrayList()
        adapter = activity?.applicationContext?.let { MoviesAdapter(it, movieList) }!!
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        progressBar.visibility = View.GONE
    }

}
