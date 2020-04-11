package com.example.iviapp.model

import com.google.gson.annotations.SerializedName

data class Token (
    @SerializedName("success")
    val isSuccess: Boolean? = null,

    @SerializedName("expires_at")
    val expiresAt: String? = null,

    @SerializedName("request_token")
    val requestToken: String? = null
)
