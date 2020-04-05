package com.example.iviapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var username = findViewById(R.id.et_username) as EditText
        var password = findViewById(R.id.et_password) as EditText
        val txtSignUp = findViewById(R.id.tv_link_sign_up) as TextView


        var loginButton = findViewById(R.id.login_btn) as Button

        var sharedPreference = getSharedPreferences("UserInfo",0)

        loginButton.setOnClickListener{
            var usernameValue = username.text.toString()
            var passwordValue = password.text.toString()

            var registeredUsername = sharedPreference.getString("username","")
            var registeredPassword = sharedPreference.getString("password","")

            if (usernameValue.equals(registeredUsername) && passwordValue.equals(registeredPassword)){
                Toast.makeText(applicationContext,"Login complete",Toast.LENGTH_SHORT).show()
                val loginComplete = Intent(applicationContext,SecondActivity::class.java)
                startActivity(loginComplete)
            }else{
                Toast.makeText(applicationContext,"Incorrect login or password",Toast.LENGTH_SHORT).show()
            }
        }


        txtSignUp.setOnClickListener{
            val toRegister = Intent(applicationContext,RegisterActivity::class.java)
            startActivity(toRegister)
        }

    }
}
