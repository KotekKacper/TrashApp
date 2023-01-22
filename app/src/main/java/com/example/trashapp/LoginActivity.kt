package com.example.trashapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.trashapp.classes.User
import com.example.trashapp.watchers.LoginWatcher
import com.example.trashapp.watchers.PasswordWatcher

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (getSharedPreferences("credentials", Context.MODE_PRIVATE).getString("login", "") != ""){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val loginEditText = findViewById<EditText>(R.id.editTextTextLoginLogIn)
        val loginWatcher = LoginWatcher(loginEditText)
        loginEditText.addTextChangedListener(loginWatcher)

        val passwordEditText = findViewById<EditText>(R.id.editTextTextPasswordLogIn)
        val passwordWatcher = PasswordWatcher(passwordEditText)
        passwordEditText.addTextChangedListener(passwordWatcher)

        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener{
            val login = findViewById<EditText>(R.id.editTextTextLoginLogIn).text.toString()
            val password = findViewById<EditText>(R.id.editTextTextPasswordLogIn).text.toString()
            val encryptedPassword = Encryption.encrypt(password)

            if (loginEditText.error == null && loginEditText.text.toString() != "" &&
                passwordEditText.error == null && passwordEditText.text.toString() != "") {
                if (DBUtils.checkLogin(this, login, encryptedPassword)){
                    val preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.putString("login", login)
                    editor.putString("password", encryptedPassword)
                    editor.apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this, "Incorrect login or password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid login data", Toast.LENGTH_SHORT).show()
            }
        }

        val loginToRegisterButton = findViewById<Button>(R.id.buttonLoginToRegister)
        loginToRegisterButton.setOnClickListener{
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}