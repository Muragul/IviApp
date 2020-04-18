package com.example.iviapp.model

import com.google.gson.annotations.SerializedName

data class SessionResponse (
    @SerializedName("success")
    val isSuccess: Boolean? = null,

    @SerializedName("session_id")
    val sessionId: String
)