package com.example.iviapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.iviapp.adapter.MoviesAdapter
import com.example.iviapp.model.Movie
import com.example.iviapp.model.MoviesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SecondFragment: Fragment() {

    lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MoviesAdapter
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var movieList: List<Movie>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: ViewGroup = inflater
            .inflate(
                R.layout.activity_second,
                container, false
            ) as ViewGroup


        recyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        swipeContainer = rootView.findViewById(R.id.main_content)
        swipeContainer.setOnRefreshListener {
            initViews()
        }
        initViews()
        return rootView
    }


    private fun initViews(){
        movieList = ArrayList()
        adapter = activity?.applicationContext?.let { MoviesAdapter(it, movieList) }!!
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.itemAnimator= DefaultItemAnimator()
        recyclerView.adapter = adapter
        adapter?.notifyDataSetChanged()

        loadJSON()
    }

    private fun loadJSON() {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                return
            }
            RetrofitService.getPostApi().getTopRatedMovieList(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .enqueue(object : Callback<MoviesResponse> {
                    override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                        swipeContainer.isRefreshing = false
                    }

                    override fun onResponse(
                        call: Call<MoviesResponse>,
                        response: Response<MoviesResponse>
                    ) {
                        Log.d("My_post_list", response.body().toString())
                        if (response.isSuccessful) {
                            val list = response.body()?.getResults()
                            adapter?.movieList = list as List<Movie>
                            adapter?.notifyDataSetChanged()
                        }
                        swipeContainer.isRefreshing = false

                    }
                })
        } catch (e: Exception) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT)
        }
    }
}