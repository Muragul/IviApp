package com.example.iviapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)
        val firstName = findViewById(R.id.reg_et_firstname) as? EditText
        val lastName = findViewById(R.id.reg_et_lastname) as? EditText
        val username = findViewById(R.id.reg_et_username) as? EditText
        val password = findViewById(R.id.reg_et_passsword) as? EditText
        val registerButton = findViewById(R.id.reg_btn) as? Button

        val txtSignIn = findViewById(R.id.tv_link_sign_in) as? TextView
        var sharedPreference = getSharedPreferences("UserInfo",0)



        if (registerButton != null) {
            registerButton.setOnClickListener{
                var firstNameValue = firstName?.text.toString()
                var lastNameValue = lastName?.text.toString()
                var usernameValue = username?.text.toString()
                var passwordValue = password?.text.toString()

                if(usernameValue.trim().length > 1){
                    var editor = sharedPreference.edit()

                    editor.putString("firstName",firstNameValue)
                    editor.putString("lastName",lastNameValue)
                    editor.putString("username",usernameValue)
                    editor.putString("password",passwordValue)
                    editor.apply()
                    Toast.makeText(applicationContext,"User registered!",Toast.LENGTH_SHORT).show()
                    val toLogin = Intent(applicationContext,MainActivity::class.java)
                    startActivity(toLogin)
                }else{
                    Toast.makeText(applicationContext,"Enter value in the fields",Toast.LENGTH_SHORT).show()
                }





            }
        }

        if (txtSignIn != null) {
            txtSignIn.setOnClickListener{
                val toSignIn = Intent(applicationContext,MainActivity::class.java)
                startActivity(toSignIn)
            }
        }


    }
}