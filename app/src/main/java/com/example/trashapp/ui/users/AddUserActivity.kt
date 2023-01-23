package com.example.trashapp.ui.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.classes.Group
import com.example.trashapp.classes.User
import com.example.trashapp.watchers.*

class AddUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val extras = intent.extras;
        if (extras != null) {
            try {
                this.findViewById<EditText>(R.id.editTextUserAccountLogin).text =
                    SpannableStringBuilder(extras.getString("login"))
                this.findViewById<EditText>(R.id.editTextUserAccountPassword).text =
                    SpannableStringBuilder(extras.getString("password"))
                this.findViewById<EditText>(R.id.editTextUserAccountEmail).text =
                    SpannableStringBuilder(extras.getString("email"))

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
                fullnameEditText.error == null &&
                phoneEditText.error == null && countryEditText.error == null &&
                cityEditText.error == null && districtEditText.error == null &&
                streetEditText.error == null && houseNumberEditText.error == null &&
                flatNumberEditText.error == null && postCodeEditText.error == null){
                DBUtils.addUser(this,
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
                        postCode = findViewById<EditText>(R.id.editTextUserAccountPostCode).text.toString()
                    )
                )
                finish()
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
                DBUtils.deleteUser(this, extras.getString("login")!!)
                finish()
            }
        }else{
            deleteButton.isVisible = false
        }



    }
}