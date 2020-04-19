package com.example.iviapp.view_model

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iviapp.BuildConfig
import com.example.iviapp.model.account.CurrentUser
import com.example.iviapp.model.network.RetrofitService
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProfileViewModel(context: Context) : ViewModel(), CoroutineScope {
    private val job = Job()
    val liveData = MutableLiveData<State>()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.complete()
    }

    fun logout(rootView: ViewGroup) {
        launch {
            liveData.value = State.ShowLoading
            try {
                val body = JsonObject().apply {
                    addProperty("session_id", CurrentUser.user.sessionId)
                }
                val response = RetrofitService.getPostApi()
                    .deleteSessionCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)
                if (response.isSuccessful)
                    liveData.value = State.Result(true)
            } catch (e: Exception) {
                liveData.value = State.Result(false)
            }
            liveData.value = State.HideLoading
        }
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class Result(val isSuccess: Boolean) : State()
    }


}