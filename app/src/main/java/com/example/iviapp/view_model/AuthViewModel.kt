package com.example.iviapp.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iviapp.BuildConfig
import com.example.iviapp.model.account.*
import com.example.iviapp.model.network.RetrofitService
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AuthViewModel(context: Context) : ViewModel(), CoroutineScope {
    private val job = Job()
    var liveData = MutableLiveData<State>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun onLogIn(login: String, password: String) {
        launch {
            liveData.value = State.ShowLoading
            try {
                val response =
                    RetrofitService.getPostApi()
                        .getTokenCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                if (response.isSuccessful) {
                    val token = Gson().fromJson(response.body(), Token::class.java)
                    if (token != null) {
                        val request = token.requestToken
                        val body = JsonObject().apply {
                            addProperty("username", login)
                            addProperty("password", password)
                            addProperty("request_token", request)
                        }
                        getLoginResponse(body)
                    }
                } else {
                    liveData.value = State.Result(false)
                }
            } catch (e: Exception) {
                liveData.value = State.Result(false)
            }
        }
    }

    private fun getLoginResponse(body: JsonObject) {
        launch {
            try {
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
                        getSession(body)
                    }
                } else {
                    liveData.value = State.Result(false)
                }
            } catch (e: java.lang.Exception) {
                liveData.value = State.Result(false)

            }
        }
    }

    private fun getSession(body: JsonObject) {
        launch {
            try {
                val response = RetrofitService.getPostApi()
                    .getSessionCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)
                if (response.isSuccessful) {
                    val session = Gson().fromJson(response.body(), SessionResponse::class.java)
                    if (session != null) {
                        val sessionId = session.sessionId
                        getAccount(sessionId)
                    }
                } else {
                    liveData.value = State.Result(false)
                }
            } catch (e: java.lang.Exception) {
                liveData.value = State.Result(false)

            }
        }
    }

    fun getAccount(session: String) {
        launch {
            try {
                val response = RetrofitService.getPostApi()
                    .getAccountCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, session)
                if (response.isSuccessful) {
                    val account = Gson().fromJson(response.body(), AccountResponse::class.java)
                    if (account != null)
                        liveData.value = State.Account(account, session)
                } else {
                    liveData.value = State.Result(false)
                }
            } catch (e: Exception) {
            } finally {
                liveData.value = State.HideLoading
            }
        }
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class Result(val isSuccess: Boolean) : State()
        data class Account(val user: AccountResponse, val session: String) : State()
    }
}