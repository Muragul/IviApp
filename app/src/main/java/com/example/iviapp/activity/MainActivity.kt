package com.example.iviapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iviapp.BuildConfig
import com.example.iviapp.R
import com.example.iviapp.RetrofitService
import com.example.iviapp.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userLogin: EditText = findViewById(R.id.login)
        val password: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            onLogIn(
                userLogin.text.toString(),
                password.text.toString()
            )
        }
    }

    private fun saveSession() {
        val savedUser: SharedPreferences =
            this.getSharedPreferences("current_user", Context.MODE_PRIVATE)
        val userEditor = savedUser.edit()
        val user: String = Gson().toJson(CurrentUser.user)
        userEditor.putString("current_user", user)
        userEditor.apply()
    }

    fun loginSuccessful(user: AccountResponse, session: String) {
        CurrentUser.user = user
        CurrentUser.user!!.sessionId = session
        saveSession()
        val intent = Intent(this, SecondActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun getAccount(session: String?) {
        var account: AccountResponse?
        RetrofitService.getPostApi().getAccount(
            BuildConfig.THE_MOVIE_DB_API_TOKEN,
            session!!
        ).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                noUserToast()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    account = Gson().fromJson(
                        response.body(),
                        AccountResponse::class.java
                    )
                    if (account != null)
                        loginSuccessful(account!!, session)
                }
            }

        })
    }

    fun getSession(body: JsonObject) {
        var session: SessionResponse?
        RetrofitService.getPostApi().getSession(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)
            .enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    noUserToast()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        session = Gson().fromJson(
                            response.body(),
                            SessionResponse::class.java
                        )
                        if (session != null) {
                            val sessionId = session!!.sessionId
                            getAccount(sessionId)
                        }
                    }

                }

            })
    }

    fun getLoginResponse(body: JsonObject) {
        var loginResponse: LoginResponse?
        RetrofitService.getPostApi().logIn(
            BuildConfig.THE_MOVIE_DB_API_TOKEN,
            body
        ).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    loginResponse = Gson().fromJson(response.body(), LoginResponse::class.java)
                    if (loginResponse != null) {
                        val body = JsonObject().apply {
                            addProperty(
                                "request_token",
                                loginResponse!!.requestToken.toString()
                            )
                        }
                        getSession(body)
                    }
                } else {
                    noUserToast()
                }
            }
        })
    }

    private fun onLogIn(login: String, password: String) {
        var token: Token?
        RetrofitService.getPostApi().getToken(BuildConfig.THE_MOVIE_DB_API_TOKEN)
            .enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    noUserToast()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        token = Gson().fromJson(response.body(), Token::class.java)
                        if (token != null) {
                            val request = token!!.requestToken
                            val body = JsonObject().apply {
                                addProperty("username", login)
                                addProperty("password", password)
                                addProperty("request_token", request)
                            }
                            getLoginResponse(body)
                        }
                    }
                }

            })
    }

    fun noUserToast() {
        Toast.makeText(this@MainActivity, "No such user", Toast.LENGTH_SHORT).show()
    }

}