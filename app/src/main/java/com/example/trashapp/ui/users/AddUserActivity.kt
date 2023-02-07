package com.example.trashapp.ui.users

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.trashapp.DBUtils
import com.example.trashapp.LoginActivity
import com.example.trashapp.R
import com.example.trashapp.classes.Group
import com.example.trashapp.classes.User
import com.example.trashapp.watchers.*

class AddUserActivity : AppCompatActivity() {
    private var adding = true
    private var login = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val tx1 = this.findViewById<TextView>(R.id.textUserAccountLogin)
        val s1 = SpannableString(tx1.text.toString()+" *")
        s1.setSpan(
            ForegroundColorSpan(Color.RED),
            s1.length-1, s1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx1.text = s1

        val tx2 = this.findViewById<TextView>(R.id.textUserAccountPassword)
        val s2 = SpannableString(tx2.text.toString()+" *")
        s2.setSpan(
            ForegroundColorSpan(Color.RED),
            s2.length-1, s2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx2.text = s2

        val tx3 = this.findViewById<TextView>(R.id.textUserAccountEmail)
        val s3 = SpannableString(tx3.text.toString()+" *")
        s3.setSpan(
            ForegroundColorSpan(Color.RED),
            s3.length-1, s3.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx3.text = s3

        val tx4 = this.findViewById<TextView>(R.id.textUserAccountFullname)
        val s4 = SpannableString(tx4.text.toString()+" *")
        s4.setSpan(
            ForegroundColorSpan(Color.RED),
            s4.length-1, s4.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx4.text = s4

        val tx5 = this.findViewById<TextView>(R.id.textUserAccountRole)
        val s5 = SpannableString(tx5.text.toString()+" *")
        s5.setSpan(
            ForegroundColorSpan(Color.RED),
            s5.length-1, s5.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx5.text = s5



        val extras = intent.extras;
        if (extras != null) {
            adding = false
            login = extras.getString("login")!!
            try {
                this.findViewById<EditText>(R.id.editTextUserAccountLogin).text =
                    SpannableStringBuilder(extras.getString("login"))
                this.findViewById<EditText>(R.id.editTextUserAccountPassword).text =
                    SpannableStringBuilder(extras.getString("password"))
                this.findViewById<EditText>(R.id.editTextUserAccountEmail).text =
                    SpannableStringBuilder(extras.getString("email"))
                this.findViewById<EditText>(R.id.editTextUserAccountRole).text =
                    SpannableStringBuilder(extras.getString("roles"))

                try {
                    this.findViewById<EditText>(R.id.editTextUserAccountFullname).text =
                        SpannableStringBuilder(extras.getString("fullname"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextUserAccountPhone).text =
                        SpannableStringBuilder(extras.getString("phone"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextUserAccountCountry).text =
                        SpannableStringBuilder(extras.getString("country"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextUserAccountCity).text =
                        SpannableStringBuilder(extras.getString("city"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextUserAccountDistrict).text =
                        SpannableStringBuilder(extras.getString("district"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextUserAccountStreet).text =
                        SpannableStringBuilder(extras.getString("street"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextUserAccountHouse).text =
                        SpannableStringBuilder(extras.getString("houseNumber"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextUserAccountFlat).text =
                        SpannableStringBuilder(extras.getString("flatNumber"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextUserAccountPostCode).text =
                        SpannableStringBuilder(extras.getString("postCode"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
            } catch (e: Exception) {
                Log.e("IntentExtras", e.toString())
            }
        }


        val loginEditText = this.findViewById<EditText>(R.id.editTextUserAccountLogin)
        val loginWatcher = LoginWatcher(loginEditText)
        loginEditText.addTextChangedListener(loginWatcher)

        val passwordEditText = this.findViewById<EditText>(R.id.editTextUserAccountPassword)
        val passwordWatcher = PasswordWatcher(passwordEditText)
        passwordEditText.addTextChangedListener(passwordWatcher)

        val emailEditText = this.findViewById<EditText>(R.id.editTextUserAccountEmail)
        val emailWatcher = EmailWatcher(emailEditText)
        emailEditText.addTextChangedListener(emailWatcher)

        val fullnameEditText = this.findViewById<EditText>(R.id.editTextUserAccountFullname)
        val fullnameWatcher = FullnameWatcher(fullnameEditText)
        fullnameEditText.addTextChangedListener(fullnameWatcher)

        val phoneEditText = this.findViewById<EditText>(R.id.editTextUserAccountPhone)
        val phoneWatcher = PhoneWatcher(phoneEditText)
        phoneEditText.addTextChangedListener(phoneWatcher)

        val countryEditText = this.findViewById<EditText>(R.id.editTextUserAccountCountry)
        val countryWatcher = CountryWatcher(countryEditText)
        countryEditText.addTextChangedListener(countryWatcher)

        val cityEditText = this.findViewById<EditText>(R.id.editTextUserAccountCity)
        val cityWatcher = CityWatcher(cityEditText)
        cityEditText.addTextChangedListener(cityWatcher)

        val districtEditText = this.findViewById<EditText>(R.id.editTextUserAccountDistrict)
        val districtWatcher = DistrictWatcher(districtEditText)
        districtEditText.addTextChangedListener(districtWatcher)

        val streetEditText = this.findViewById<EditText>(R.id.editTextUserAccountStreet)
        val streetWatcher = StreetWatcher(streetEditText)
        streetEditText.addTextChangedListener(streetWatcher)

        val houseNumberEditText = this.findViewById<EditText>(R.id.editTextUserAccountHouse)
        val houseNumberWatcher = HouseNumberWatcher(houseNumberEditText)
        houseNumberEditText.addTextChangedListener(houseNumberWatcher)

        val flatNumberEditText = this.findViewById<EditText>(R.id.editTextUserAccountFlat)
        val flatNumberWatcher = FlatNumberWatcher(flatNumberEditText)
        flatNumberEditText.addTextChangedListener(flatNumberWatcher)

        val postCodeEditText = this.findViewById<EditText>(R.id.editTextUserAccountPostCode)
        val postCodeWatcher = PostCodeWatcher(postCodeEditText)
        postCodeEditText.addTextChangedListener(postCodeWatcher)

        val roleEditText = findViewById<EditText>(R.id.editTextUserAccountRole)
        roleEditText.addTextChangedListener(ListWatcher(roleEditText))

        if (loginEditText.text.toString() == "admin"){
            loginEditText.isEnabled = false
            roleEditText.isEnabled = false
            findViewById<Button>(R.id.buttonUserDelete).isVisible = false
        }

        val anyChangeWatcher = AnyChangeWatcher(arrayListOf(
            loginEditText,
            passwordEditText,
            emailEditText,
            fullnameEditText,
            phoneEditText,
            countryEditText,
            cityEditText,
            districtEditText,
            streetEditText,
            houseNumberEditText,
            flatNumberEditText,
            postCodeEditText),
            arrayListOf("login", "password", "", "", "", "", "", "", "", "", "", ""),
            this.findViewById<Button>(R.id.buttonUserConfirm))
        loginEditText.addTextChangedListener(anyChangeWatcher)
        passwordEditText.addTextChangedListener(anyChangeWatcher)
        emailEditText.addTextChangedListener(anyChangeWatcher)
        fullnameEditText.addTextChangedListener(anyChangeWatcher)
        phoneEditText.addTextChangedListener(anyChangeWatcher)
        countryEditText.addTextChangedListener(anyChangeWatcher)
        cityEditText.addTextChangedListener(anyChangeWatcher)
        districtEditText.addTextChangedListener(anyChangeWatcher)
        streetEditText.addTextChangedListener(anyChangeWatcher)
        houseNumberEditText.addTextChangedListener(anyChangeWatcher)
        flatNumberEditText.addTextChangedListener(anyChangeWatcher)
        postCodeEditText.addTextChangedListener(anyChangeWatcher)

        val applyButton = findViewById<Button>(R.id.buttonUserConfirm)
        applyButton.setOnClickListener{
            if (loginEditText.error == null && loginEditText.text.toString() != "" &&
                passwordEditText.error == null && passwordEditText.text.toString() != "" &&
                emailEditText.error == null && emailEditText.text.toString() != "" &&
                fullnameEditText.error == null && fullnameEditText.text.toString() != "" &&
                phoneEditText.error == null && countryEditText.error == null &&
                cityEditText.error == null && districtEditText.error == null &&
                streetEditText.error == null && houseNumberEditText.error == null &&
                flatNumberEditText.error == null && postCodeEditText.error == null &&
                roleEditText.error == null && roleEditText.text.toString() != ""){
                DBUtils.addUser(this, adding,
                    User(login = findViewById<EditText>(R.id.editTextUserAccountLogin).text.toString(),
                        password = findViewById<EditText>(R.id.editTextUserAccountPassword).text.toString(),
                        email = findViewById<EditText>(R.id.editTextUserAccountEmail).text.toString(),
                        phone = findViewById<EditText>(R.id.editTextUserAccountPhone).text.toString(),
                        fullname = findViewById<EditText>(R.id.editTextUserAccountFullname).text.toString(),
                        country = findViewById<EditText>(R.id.editTextUserAccountCountry).text.toString(),
                        city = findViewById<EditText>(R.id.editTextUserAccountCity).text.toString(),
                        district = findViewById<EditText>(R.id.editTextUserAccountDistrict).text.toString(),
                        street = findViewById<EditText>(R.id.editTextUserAccountStreet).text.toString(),
                        houseNumber = findViewById<EditText>(R.id.editTextUserAccountHouse).text.toString(),
                        flatNumber = findViewById<EditText>(R.id.editTextUserAccountFlat).text.toString(),
                        postCode = findViewById<EditText>(R.id.editTextUserAccountPostCode).text.toString(),
                        roles = ArrayList(findViewById<EditText>(R.id.editTextUserAccountRole).text.toString().uppercase().split(","))
                    ), login
                )
            } else{
                Toast.makeText(this, "Invalid user data", Toast.LENGTH_SHORT).show()
            }
        }
        val cancelButton = findViewById<Button>(R.id.buttonUserCancel)
        cancelButton.setOnClickListener {
            finish()
        }

        val deleteButton = findViewById<Button>(R.id.buttonUserDelete)
        if (extras != null) {
            deleteButton.setOnClickListener {
                DBUtils.deleteUser(this, login)
                if (login == getSharedPreferences("credentials", Context.MODE_PRIVATE).getString("login", "")){
                    val editor = getSharedPreferences("credentials", Context.MODE_PRIVATE)!!.edit()
                    editor.putString("login", "")
                    editor.putString("password", "")
                    editor.apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                finish()
            }
        }else{
            deleteButton.isVisible = false
        }

        val role = getSharedPreferences("credentials", Context.MODE_PRIVATE)
            ?.getString("role", "")?.split(",")
        if (role != null) {
            when {
                role.contains("ADMIN") -> {
                    roleEditText.isEnabled = true
                    deleteButton.text = "Delete user"
                }
                role.contains("USER") -> {
                    roleEditText.isEnabled = false
                    deleteButton.text = "Delete account"
                }
                else -> {
                    roleEditText.isEnabled = false
                    deleteButton.text = "Delete account"
                }
            }
        }

    }
}