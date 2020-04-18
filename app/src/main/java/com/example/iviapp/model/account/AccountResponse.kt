package com.example.iviapp.model.account

import com.google.gson.annotations.SerializedName

data class AccountResponse (
    @SerializedName("id")
    val accountId: Int,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("username")
    val userName: String? = null,

    @SerializedName("include_adult")
    val isAdult: Boolean? = null,

    var sessionId: String? = null
)