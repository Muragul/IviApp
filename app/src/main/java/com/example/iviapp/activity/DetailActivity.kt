package com.example.iviapp.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.iviapp.BuildConfig
import com.example.iviapp.R
import com.example.iviapp.api.RetrofitService
import com.example.iviapp.model.Movie
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    lateinit var poster: ImageView
    lateinit var movieTitle: TextView
    lateinit var releaseDate: TextView
    lateinit var adult: TextView
    lateinit var rating: TextView
    lateinit var popularity: TextView
    lateinit var overview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        poster = findViewById(R.id.poster)
        movieTitle = findViewById(R.id.title)
        releaseDate = findViewById(R.id.releasedate)
        adult = findViewById(R.id.adult)
        rating = findViewById(R.id.rate)
        popularity = findViewById(R.id.popularity)
        val back: ImageButton = findViewById(R.id.back)
        overview = findViewById(R.id.overview)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else {
            var decorView: View = window.decorView
            var options: Int = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = options
        }

        val id = intent.getIntExtra("movie_id", 1)

        RetrofitService.getPostApi().getMovie(
            id,
            BuildConfig.THE_MOVIE_DB_API_TOKEN
        )
            .enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        this@DetailActivity,
                        "Can not find",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    onBackPressed()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val movie: Movie = Gson().fromJson(
                            response.body(),
                            Movie::class.java
                        )
                        fillViews(movie)
                    }
                }

            })

        back.setOnClickListener {
            onBackPressed()
        }
    }

    fun fillViews(movie: Movie) {
        overview.text = movie.overview
        Glide.with(this@DetailActivity).load(movie.getBackdropPath())
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