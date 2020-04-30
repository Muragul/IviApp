package com.example.iviapp.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.crashlytics.android.Crashlytics
import com.example.iviapp.R
import com.example.iviapp.model.account.*
import com.example.iviapp.view_model.AuthViewModel
import com.example.iviapp.view_model.ViewModelProviderFactory
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.profile_page.*

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var authViewModel: AuthViewModel
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        val userLogin: EditText = findViewById(R.id.login)
        val password: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login_button)


        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "item_id")

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle)


        val viewModelProviderFactory = ViewModelProviderFactory(this)
        authViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(AuthViewModel::class.java)
        authViewModel.liveData.observe(this, Observer { result ->
            when (result) {
                is AuthViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is AuthViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                }
                is AuthViewModel.State.Result -> {
                    if (!result.isSuccess)
                        noUserToast()
                }
                is AuthViewModel.State.Account -> {
                    loginSuccessful(result.user, result.session)
                }
            }
        })
        loginButton.setOnClickListener {
            authViewModel.onLogIn(
                userLogin.text.toString(),
                password.text.toString()
            )
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
        val intent = Intent(this, SecondActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun noUserToast() {
        Toast.makeText(this@MainActivity, "No such user", Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.GONE
    }

    private fun setKey(key: String){
        Crashlytics.setString(key, "foo")

        Crashlytics.setBool(key, true )

        Crashlytics.setDouble(key, 1.0)

        Crashlytics.setFloat(key, 1.0f)

        Crashlytics.setInt(key, 1)
    }


}