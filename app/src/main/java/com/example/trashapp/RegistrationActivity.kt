package com.example.trashapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.trashapp.classes.User

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener{
            val user = User(
                login = findViewById<EditText>(R.id.editTextTextLogin).text.toString(),
                password = findViewById<EditText>(R.id.editTextTextPassword).text.toString(),
                email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
            )
            DBUtils.addUser(this, user)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val registerToLoginButton = findViewById<Button>(R.id.buttonRegisterToLogin)
        registerToLoginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}