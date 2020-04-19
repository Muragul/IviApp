package com.example.iviapp.view.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.iviapp.*
import com.example.iviapp.model.movie.Movie
import com.example.iviapp.view_model.MovieDetailViewModel
import com.example.iviapp.view_model.ViewModelProviderFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var poster: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var releaseDate: TextView
    lateinit var adult: TextView
    lateinit var rating: TextView
    lateinit var popularity: TextView
    lateinit var overview: TextView
    lateinit var save: ImageButton
    private var isFavoriteMovie: Boolean = false
    private var movieId: Int = 1
    private lateinit var progressBar: ProgressBar
    private lateinit var movieDetailViewModel: MovieDetailViewModel

    companion object {
        var needToSycn: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val viewModelProviderFactory = ViewModelProviderFactory(this)
        movieDetailViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(MovieDetailViewModel::class.java)

        poster = findViewById(R.id.poster)
        movieTitle = findViewById(R.id.title)
        releaseDate = findViewById(R.id.releasedate)
        adult = findViewById(R.id.adult)
        rating = findViewById(R.id.rate)
        popularity = findViewById(R.id.popularity)
        overview = findViewById(R.id.overview)
        val back: ImageButton = findViewById(R.id.back)
        save = findViewById(R.id.save)
        progressBar = findViewById(R.id.progressBar)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else {
            val decorView: View = window.decorView
            val options: Int = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = options
        }

        movieId = intent.getIntExtra("movie_id", 1)
        movieDetailViewModel.isFavoriteMovie(movieId)
        movieDetailViewModel.getMovie(movieId)
        movieDetailViewModel.liveData.observe(this, Observer { result ->
            when (result) {
                is MovieDetailViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is MovieDetailViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                }
                is MovieDetailViewModel.State.Result -> {
                    fillMovieData(result.movie)
                }
                is MovieDetailViewModel.State.IsFavorite -> {
                    isFavoriteMovie = result.isFavorite
                    if (isFavoriteMovie) {
                        Glide.with(this).load(R.drawable.ic_turned).into(save)
                    } else {
                        Glide.with(this).load(R.drawable.ic_turned_in).into(save)
                    }
                }
            }
        })

        back.setOnClickListener {
            onBackPressed()
        }

        save.setOnClickListener {
            if (isFavoriteMovie) {
                Glide.with(this).load(R.drawable.ic_turned_in).into(save)
                isFavoriteMovie = false
            } else {
                Glide.with(this).load(R.drawable.ic_turned).into(save)
                isFavoriteMovie = true
            }
            movieDetailViewModel.likeMovie(isFavoriteMovie, movieId)
        }

    }

    private fun fillMovieData(movie: Movie) {
        overview.text = movie.overview
        Glide.with(this@DetailActivity).load(movie.getPosterPath())
            .into(poster)
        movieTitle.text = movie.originalTitle
        releaseDate.text = movie.releaseDate
        if (movie.adult)
            adult.text = "18+"
        else
            adult.text = "No"
        rating.text = movie.voteAverage.toString()
        popularity.text = movie.popularity.toString()
    }

}