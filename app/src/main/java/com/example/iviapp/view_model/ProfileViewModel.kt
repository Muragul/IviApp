package com.example.iviapp.view_model

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.example.iviapp.BuildConfig
import com.example.iviapp.model.account.CurrentUser
import com.example.iviapp.model.network.RetrofitService
import com.example.iviapp.view.activity.MainActivity
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProfileViewModel(context: Context) : ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.complete()
    }

    fun logout(rootView: ViewGroup) {
        launch {
            try {
                val body = JsonObject().apply {
                    addProperty("session_id", CurrentUser.user.sessionId)
                }
                val response = RetrofitService.getPostApi()
                    .deleteSessionCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)
                if (response.isSuccessful) {
                    val savedUser: SharedPreferences = rootView.context.getSharedPreferences(
                        "current_user",
                        Context.MODE_PRIVATE
                    )
                    savedUser.edit().remove("current_user").apply()
                }
            } catch (e: Exception) {
            }
        }
    }


}