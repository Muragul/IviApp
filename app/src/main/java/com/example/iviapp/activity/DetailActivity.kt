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
import com.example.iviapp.RetrofitService
import com.example.iviapp.model.CurrentUser
import com.example.iviapp.model.FavoriteResponse
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
    lateinit var save: ImageButton
    var isFav: Boolean = false

    var movie_id: Int? = null
    var account_id: Int? = null
    var session_id: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        poster = findViewById(R.id.poster)
        movieTitle = findViewById(R.id.title)
        releaseDate = findViewById(R.id.releasedate)
        adult = findViewById(R.id.adult)
        rating = findViewById(R.id.rate)
        popularity = findViewById(R.id.popularity)
        overview = findViewById(R.id.overview)
        val back: ImageButton = findViewById(R.id.back)
        save = findViewById(R.id.save)


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
        movie_id = id
        session_id = CurrentUser.user?.sessionId
        account_id = CurrentUser.user?.accountId

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
                        hasLike()
                    }
                }

            })

        back.setOnClickListener {
            onBackPressed()
        }

        save.setOnClickListener {
            if (isFav){
                Glide.with(this).load(R.drawable.ic_turned_in).into(save)
            } else {
                Glide.with(this).load(R.drawable.ic_turned).into(save)
            }
            likeMovie(!isFav)
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


    private fun hasLike() {
        RetrofitService.getPostApi()
            .hasLike(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN, session_id)
            .enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        var like = Gson().fromJson(
                            response.body(),
                            FavoriteResponse::class.java
                        ).favorite
                        if (like){
                            save.setImageResource(R.drawable.ic_turned)
                            isFav = true
                        }
                        else {
                            save.setImageResource(R.drawable.ic_turned_in)
                            isFav = false
                        }
                    }

                }
            })

    }

    private fun likeMovie(favourite: Boolean) {
        val body = JsonObject().apply {
            addProperty("media_type", "movie")
            addProperty("media_id", movie_id)
            addProperty("favorite", favourite)
        }

        RetrofitService.getPostApi()
            .rate(account_id, BuildConfig.THE_MOVIE_DB_API_TOKEN, session_id, body)
            .enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                }
            })
    }


}