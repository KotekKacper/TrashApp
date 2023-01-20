package com.example.trashapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener{
            // TODO - add the user to DB
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val registerToLoginButton = findViewById<Button>(R.id.buttonRegisterToLogin)
        registerToLoginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}