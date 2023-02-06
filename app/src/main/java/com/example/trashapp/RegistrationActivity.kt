package com.example.trashapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import com.example.trashapp.classes.Role
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.trashapp.classes.User
import com.example.trashapp.watchers.*


class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val loginEditText = findViewById<EditText>(R.id.editTextTextLogin)
        val loginWatcher = LoginWatcher(loginEditText)
        loginEditText.addTextChangedListener(loginWatcher)

        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
        val passwordWatcher = PasswordWatcher(passwordEditText)
        passwordEditText.addTextChangedListener(passwordWatcher)

        val repeatPasswordEditText = findViewById<EditText>(R.id.editTextTextRepeatPassword)
        val repeatPasswordWatcher = RepeatPasswordWatcher(repeatPasswordEditText, passwordEditText)
        repeatPasswordEditText.addTextChangedListener(repeatPasswordWatcher)

        val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val emailWatcher = EmailWatcher(emailEditText)
        emailEditText.addTextChangedListener(emailWatcher)

        val repeatEmailEditText = findViewById<EditText>(R.id.editTextTextRepeatEmailAddress)
        val repeatEmailWatcher = RepeatEmailWatcher(repeatEmailEditText, emailEditText)
        repeatEmailEditText.addTextChangedListener(repeatEmailWatcher)

        val fullnameEditText = findViewById<EditText>(R.id.editTextTextFullname)
        val fullnameWatcher = FullnameWatcher(fullnameEditText)
        fullnameEditText.addTextChangedListener(fullnameWatcher)

        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener{

            if (loginEditText.error == null && loginEditText.text.isNotEmpty() &&
                passwordEditText.error == null && passwordEditText.text.isNotEmpty() &&
                repeatPasswordEditText.error == null && repeatPasswordEditText.text.isNotEmpty() &&
                emailEditText.error == null && emailEditText.text.isNotEmpty() &&
                repeatEmailEditText.error == null && repeatEmailEditText.text.isNotEmpty() &&
                fullnameEditText.error == null && fullnameEditText.text.isNotEmpty()) {
                val user = User(
                    login = findViewById<EditText>(R.id.editTextTextLogin).text.toString(),
                    password = findViewById<EditText>(R.id.editTextTextPassword).text.toString(),
                    email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString(),
                    roles = arrayListOf("USER"),
                    fullname = findViewById<EditText>(R.id.editTextTextFullname).text.toString()
                )
                DBUtils.addUserRegister(this, true, user)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid registration data", Toast.LENGTH_SHORT).show()
            }
        }

        val registerToLoginButton = findViewById<Button>(R.id.buttonRegisterToLogin)
        registerToLoginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}