package com.example.iviapp.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.iviapp.BuildConfig
import com.example.iviapp.R
import com.example.iviapp.activity.MainActivity
import com.example.iviapp.RetrofitService
import com.example.iviapp.model.CurrentUser
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class ThirdFragment : Fragment(), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.complete()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: ViewGroup = inflater
            .inflate(
                R.layout.profile_page,
                container, false
            ) as ViewGroup
        val name: TextView = rootView.findViewById(R.id.name)
        val logoutBtn: Button = rootView.findViewById(R.id.logout)
        name.text = CurrentUser.user.userName
        logoutBtn.setOnClickListener {
            logoutCoroutine(rootView)
        }
        return rootView
    }

    fun logoutCoroutine(rootView: ViewGroup) {
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
                    val intent = Intent(rootView.context, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            } catch (e: Exception) {

            }
        }
    }

}
