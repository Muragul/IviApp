package com.example.iviapp.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.iviapp.R
import com.example.iviapp.model.account.AccountResponse
import com.example.iviapp.model.account.CurrentUser
import com.example.iviapp.model.cinema.Cinema
import com.example.iviapp.view_model.AuthViewModel
import com.example.iviapp.view_model.CinemaListViewModel
import com.example.iviapp.view_model.ViewModelProviderFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class StartActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var cinemaListViewModel: CinemaListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val viewModelProviderFactory = ViewModelProviderFactory(this)
        cinemaListViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(CinemaListViewModel::class.java)
        cinemaListViewModel.addCinemaListToDatabase(cinemaListGenerator())

        authViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(AuthViewModel::class.java)
        authViewModel.liveData.observe(this, Observer { result ->
            when (result) {
                is AuthViewModel.State.Result -> {
                    if (!result.isSuccess) {
                        val intent = Intent(this@StartActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
                is AuthViewModel.State.Account -> {
                    loginSuccessful(result.user, result.session)
                }
            }
        })

        val savedUser: SharedPreferences =
            this.getSharedPreferences("current_user", Context.MODE_PRIVATE)
        val user = savedUser.getString("current_user", null)
        if (user != null) {
            val type: Type = object : TypeToken<AccountResponse>() {}.type
            CurrentUser.user = Gson().fromJson(user, type)
            if (CurrentUser.user.sessionId != null) {
                authViewModel.getAccount(CurrentUser.user.sessionId.toString())
                val intent = Intent(this, SecondActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        } else {
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
    }

    fun cinemaListGenerator(): List<Cinema> {
        val cinema = Cinema(
            1,
            "Bekmambetov Cinema",
            "пл. Республики 13, Алматыпл. Республики 13, Алматы",
            43.23795,
            76.945586
        )
        val cinema1 = Cinema(
            2,
            "CINEMAX Dostyk Multiplex",
            "мкр.Самал 2, 111 д. в ТРЦ \"Dostyk Plaza\"",
            43.232974,
            76.955679
        )
        val cinema2 = Cinema(
            3,
            "Lumiera Cinema",
            "проспект Абылай Хана 62, Алматы",
            43.262106,
            76.941394
        )
        val cinema3 = Cinema(
            4,
            "Kinopark 11 IMAX Esentai",
            "проспект Аль-Фараби, 77/8 Esentai Mall, Алматы",
            43.218559,
            76.927724
        )
        val cinema4 = Cinema(
            5,
            "Cinema Towers 3D",
            "улица Байзакова 280, Алматы",
            43.237484,
            76.91504
        )
        val cinema5 = Cinema(
            6,
            "Chaplin Cinemas(Mega)",
            "город, ул. Макатаева 127/9, Алматы",
            43.264043,
            76.929472
        )
        val cinema6 = Cinema(
            7,
            "Арман 3D",
            "ТРЦ \"MART\"",
            43.336739,
            76.952996
        )
        val cinema7 = Cinema(
            8,
            "Nomad Cinema",
            "город, проспект Рыскулова 103, Алматы",
            43.267059,
            76.87022
        )
        val cinema8 = Cinema(
            9,
            "Kinopark 6 Sputnik",
            "Мамыр, микрорайон 1, 8а ТРЦ \"Sputnik Mall, Алматы",
            43.211989,
            76.842285
        )
        val cinema9 = Cinema(
            10,
            "Kinopark 8 Moskva",
            "8 микрорайон, 37/1 ТРЦ MOSKVA Metropolitan, Алматы",
            43.226885,
            76.864132
        )
        val cinema10 = Cinema(
            11,
            "Kinopark 5 Atakent",
            "ул. Тимирязева, 42 к3 ТРК \"Atakent Mall, Алматы",
            43.225622,
            76.907748
        )
        val cinema11 = Cinema(
            12,
            "Kinopark 16 Forum",
            "пр. Сейфуллина, 615 ТРЦ \"Forum",
            43.234228,
            76.935831
        )
        val list: MutableList<Cinema> = ArrayList()
        list.add(cinema)
        list.add(cinema1)
        list.add(cinema2)
        list.add(cinema3)
        list.add(cinema5)
        list.add(cinema6)
        list.add(cinema7)
        list.add(cinema8)
        list.add(cinema9)
        list.add(cinema10)
        list.add(cinema11)
        list.add(cinema4)
        return list
    }

}
