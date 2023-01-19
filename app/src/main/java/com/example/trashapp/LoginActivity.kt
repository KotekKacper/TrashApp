package com.example.trashapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val loginToRegisterButton = findViewById<Button>(R.id.buttonLoginToRegister)
        loginToRegisterButton.setOnClickListener{
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}