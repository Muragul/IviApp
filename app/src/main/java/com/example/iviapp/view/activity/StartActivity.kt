package com.example.iviapp.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.iviapp.BuildConfig
import com.example.iviapp.R
import com.example.iviapp.model.RetrofitService
import com.example.iviapp.model.AccountResponse
import com.example.iviapp.model.CurrentUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext

class StartActivity : AppCompatActivity(), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val savedUser: SharedPreferences =
            this.getSharedPreferences("current_user", Context.MODE_PRIVATE)
        val user = savedUser.getString("current_user", null)
        if (user != null) {
            val type: Type = object : TypeToken<AccountResponse>() {}.type
            CurrentUser.user = Gson().fromJson<AccountResponse>(user, type)
            if (CurrentUser.user.sessionId != null)
                getSavedAccountCoroutine(CurrentUser.user.sessionId.toString())
        }
        else {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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

    private fun loginSuccessful(user: AccountResponse, session: String) {
        CurrentUser.user = user
        CurrentUser.user.sessionId = session
        saveSession()
        val intent = Intent(this, SecondActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun getSavedAccountCoroutine(session: String) {
        launch {
            try {
                val response = RetrofitService.getPostApi()
                    .getAccountCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, session)
                if (response.isSuccessful) {
                    val account = Gson().fromJson(response.body(), AccountResponse::class.java)
                    if (account != null)
                        loginSuccessful(account, session)
                    else {
                        val intent = Intent(this@StartActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                val intent = Intent(this@StartActivity, SecondActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

}
