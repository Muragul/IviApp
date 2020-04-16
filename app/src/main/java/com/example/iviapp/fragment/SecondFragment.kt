package com.example.iviapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.iviapp.*
import com.example.iviapp.activity.DetailActivity
import com.example.iviapp.adapter.MoviesAdapter
import com.example.iviapp.model.CurrentUser
import com.example.iviapp.model.Movie
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class SecondFragment : Fragment(), CoroutineScope {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MoviesAdapter
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var movieList: List<Movie>
    private val job = Job()
    private var movieDao: MovieDao? = null
    private lateinit var progressBar: ProgressBar

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: ViewGroup = inflater
            .inflate(
                R.layout.activity_second,
                container, false
            ) as ViewGroup

        val toolbar: TextView = rootView.findViewById(R.id.toolbar)
        toolbar.text = "Favorites"

        movieDao = MovieDatabase.getDatabase(activity as Context).movieDao()
        progressBar = rootView.findViewById(R.id.progressBar)
        recyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        swipeContainer = rootView.findViewById(R.id.main_content)
        swipeContainer.setOnRefreshListener {
            initViews()
        }
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

        getFavoritesCoroutine()
        progressBar.visibility = View.GONE
    }

    private fun getFavoritesCoroutine() {
        launch {
            swipeContainer.isRefreshing = true
            val list = withContext(Dispatchers.IO) {
                try {
                    if (DetailActivity.needToSycn) {
                        val savedMovieList = movieDao?.getAll()
                        if (savedMovieList != null)
                            for (movie in savedMovieList) {
                                val body = JsonObject().apply {
                                    addProperty("media_type", "movie")
                                    addProperty("media_id", movie.id)
                                    addProperty("favorite", movie.isFavorite)
                                }
                                RetrofitService.getPostApi().rateCoroutine(
                                    CurrentUser.user?.accountId,
                                    BuildConfig.THE_MOVIE_DB_API_TOKEN,
                                    CurrentUser.user?.sessionId,
                                    body
                                )
                            }
                        DetailActivity.needToSycn = false
                    }
                    val response = RetrofitService.getPostApi()
                        .getFavoritesCoroutine(
                            CurrentUser.user?.accountId!!,
                            BuildConfig.THE_MOVIE_DB_API_TOKEN,
                            CurrentUser.user?.sessionId.toString()
                        )
                    if (response.isSuccessful) {
                        val result = response.body()?.getResults()
                        if (!result.isNullOrEmpty()) {
                            for (movie in result)
                                movie?.isFavorite = true
                            movieDao?.insertAll(result as List<Movie>)
                        }
                        result
                    } else {
                        movieDao?.getFavorite() ?: emptyList()
                    }
                } catch (e: Exception) {
                    movieDao?.getFavorite() ?: emptyList()
                }
            }
            adapter.movieList = list as List<Movie>
            adapter.notifyDataSetChanged()
            swipeContainer.isRefreshing = false
        }
    }

}