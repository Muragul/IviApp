package com.example.iviapp.model

class CurrentUser {
    companion object{
        var user: AccountResponse? = null
        var favorites: List<Movie>? = null
    }
}