package com.example.iviapp.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.iviapp.BuildConfig
import com.example.iviapp.model.account.AccountResponse
import com.example.iviapp.model.account.CurrentUser
import com.example.iviapp.model.network.RetrofitService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class AuthViewModel(context: Context) : ViewModel(), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getSavedAccount(session: String) {
        launch {
            try {
                val response = RetrofitService.getPostApi()
                    .getAccountCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, session)
                if (response.isSuccessful) {
                    val account = Gson().fromJson(response.body(), AccountResponse::class.java)
                    if (account != null)
                        loginSuccessful(account, session)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun loginSuccessful(user: AccountResponse, session: String) {
        CurrentUser.user = user
        CurrentUser.user.sessionId = session
        saveSession()
    }

    private fun saveSession() {
    }

}