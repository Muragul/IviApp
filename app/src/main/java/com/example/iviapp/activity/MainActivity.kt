package com.example.iviapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iviapp.BuildConfig
import com.example.iviapp.R
import com.example.iviapp.RetrofitService
import com.example.iviapp.model.*
import com.example.iviapp.model.CurrentUser.Companion.user
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()
    private lateinit var progressBar: ProgressBar
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        val userLogin: EditText = findViewById(R.id.login)
        val password: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            onLogInCoroutine(
                userLogin.text.toString(),
                password.text.toString()
            )
        }
    }

    private fun saveSession() {
        val savedUser: SharedPreferences =
            this.getSharedPreferences("current_user", Context.MODE_PRIVATE)
        val userEditor = savedUser.edit()
        val user: String = Gson().toJson(user)
        userEditor.putString("current_user", user)
        userEditor.apply()
    }

    private fun loginSuccessful(user: AccountResponse, session: String) {
        CurrentUser.user = user
        CurrentUser.user.sessionId = session
        saveSession()
        val intent = Intent(this, SecondActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun getAccountCoroutine(session: String) {
        launch {
            val response = RetrofitService.getPostApi()
                .getAccountCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, session)
            if (response.isSuccessful) {
                val account = Gson().fromJson(response.body(), AccountResponse::class.java)
                if (account != null)
                    loginSuccessful(account, session)
            } else
                noUserToast()
        }
    }


    private fun getSessionCoroutine(body: JsonObject) {
        launch {
            val response = RetrofitService.getPostApi()
                .getSessionCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)
            if (response.isSuccessful) {
                val session = Gson().fromJson(response.body(), SessionResponse::class.java)
                if (session != null) {
                    val sessionId = session.sessionId
                    getAccountCoroutine(sessionId)
                }
            } else
                noUserToast()
        }
    }

    private fun getLoginResponseCoroutine(body: JsonObject) {
        launch {
            val response = RetrofitService.getPostApi()
                .logInCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)
            if (response.isSuccessful) {
                val loginResponse = Gson().fromJson(response.body(), LoginResponse::class.java)
                if (loginResponse != null) {
                    val body = JsonObject().apply {
                        addProperty(
                            "request_token",
                            loginResponse.requestToken.toString()
                        )
                    }
                    getSessionCoroutine(body)
                } else
                    noUserToast()
            }
        }
    }

    private fun onLogInCoroutine(login: String, password: String) {
        launch {
            val response =
                RetrofitService.getPostApi().getTokenCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN)
            if (response.isSuccessful) {
                val token = Gson().fromJson(response.body(), Token::class.java)
                if (token != null) {
                    val request = token.requestToken
                    val body = JsonObject().apply {
                        addProperty("username", login)
                        addProperty("password", password)
                        addProperty("request_token", request)
                    }
                    getLoginResponseCoroutine(body)
                }
            } else
                noUserToast()
        }
    }

    private fun noUserToast() {
        Toast.makeText(this@MainActivity, "No such user", Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.GONE
    }

}