package com.example.iviapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iviapp.BuildConfig
import com.example.iviapp.R
import com.example.iviapp.api.RetrofitService
import com.example.iviapp.model.AccountResponse
import com.example.iviapp.model.CurrentUser
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val savedUser: SharedPreferences =
            this.getSharedPreferences("current_user", Context.MODE_PRIVATE)
        val user = savedUser.getString("current_user", null)
        val type: Type = object : TypeToken<AccountResponse>() {}.type
        CurrentUser.user = Gson().fromJson<AccountResponse>(user, type)

        if (CurrentUser.user != null && CurrentUser.user!!.sessionId != null)
            getSavedAccount(CurrentUser.user!!.sessionId.toString())
        else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
        startActivity(intent)
    }

    private fun getSavedAccount(session: String) {
        var account: AccountResponse
        RetrofitService.getPostApi().getAccount(
            BuildConfig.THE_MOVIE_DB_API_TOKEN,
            session
        ).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    account = Gson().fromJson(
                        response.body(),
                        AccountResponse::class.java
                    )
                    if (account != null)
                        loginSuccessful(account, session)
                    else {
                        CurrentUser.user = null
                        val intent = Intent(
                            this@StartActivity,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                    }
                }
            }

        })
    }

}
