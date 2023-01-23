package com.example.trashapp.ui.companies

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
import com.example.trashapp.classes.CleaningCompany
import com.example.trashapp.classes.User
import com.example.trashapp.watchers.*

class AddCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_company)

        val extras = intent.extras;
        if (extras != null) {
            try {
                this.findViewById<EditText>(R.id.editTextCompanyNIP).text =
                    SpannableStringBuilder(extras.getString("nip"))
                this.findViewById<EditText>(R.id.editTextCompanyEmail).text =
                    SpannableStringBuilder(extras.getString("email"))
                this.findViewById<EditText>(R.id.editTextCompanyPhone).text =
                    SpannableStringBuilder(extras.getString("phone"))

                try {
                    this.findViewById<EditText>(R.id.editTextCompanyCountry).text =
                        SpannableStringBuilder(extras.getString("country"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextCompanyCity).text =
                        SpannableStringBuilder(extras.getString("city"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextCompanyDistrict).text =
                        SpannableStringBuilder(extras.getString("district"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextCompanyStreet).text =
                        SpannableStringBuilder(extras.getString("street"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextCompanyHouse).text =
                        SpannableStringBuilder(extras.getString("houseNumber"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextCompanyFlat).text =
                        SpannableStringBuilder(extras.getString("flatNumber"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextCompanyPostCode).text =
                        SpannableStringBuilder(extras.getString("postCode"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
            } catch (e: Exception) {
                Log.e("IntentExtras", e.toString())
            }
        }


        val nipEditText = this.findViewById<EditText>(R.id.editTextCompanyNIP)
        val nipWatcher = NipWatcher(nipEditText)
        nipEditText.addTextChangedListener(nipWatcher)

        val emailEditText = this.findViewById<EditText>(R.id.editTextCompanyEmail)
        val emailWatcher = EmailWatcher(emailEditText)
        emailEditText.addTextChangedListener(emailWatcher)

        val phoneEditText = this.findViewById<EditText>(R.id.editTextCompanyPhone)
        val phoneWatcher = PhoneWatcher(phoneEditText)
        phoneEditText.addTextChangedListener(phoneWatcher)

        val countryEditText = this.findViewById<EditText>(R.id.editTextCompanyCountry)
        val countryWatcher = CountryWatcher(countryEditText)
        countryEditText.addTextChangedListener(countryWatcher)

        val cityEditText = this.findViewById<EditText>(R.id.editTextCompanyCity)
        val cityWatcher = CityWatcher(cityEditText)
        cityEditText.addTextChangedListener(cityWatcher)

        val districtEditText = this.findViewById<EditText>(R.id.editTextCompanyDistrict)
        val districtWatcher = DistrictWatcher(districtEditText)
        districtEditText.addTextChangedListener(districtWatcher)

        val streetEditText = this.findViewById<EditText>(R.id.editTextCompanyStreet)
        val streetWatcher = StreetWatcher(streetEditText)
        streetEditText.addTextChangedListener(streetWatcher)

        val houseNumberEditText = this.findViewById<EditText>(R.id.editTextCompanyHouse)
        val houseNumberWatcher = HouseNumberWatcher(houseNumberEditText)
        houseNumberEditText.addTextChangedListener(houseNumberWatcher)

        val flatNumberEditText = this.findViewById<EditText>(R.id.editTextCompanyFlat)
        val flatNumberWatcher = FlatNumberWatcher(flatNumberEditText)
        flatNumberEditText.addTextChangedListener(flatNumberWatcher)

        val postCodeEditText = this.findViewById<EditText>(R.id.editTextCompanyPostCode)
        val postCodeWatcher = PostCodeWatcher(postCodeEditText)
        postCodeEditText.addTextChangedListener(postCodeWatcher)


        val applyButton = findViewById<Button>(R.id.buttonCompanyConfirm)
        applyButton.setOnClickListener{
            if (nipEditText.error == null && nipEditText.text.toString() != "" &&
                emailEditText.error == null && emailEditText.text.toString() != "" &&
                phoneEditText.error == null && phoneEditText.text.toString() != "" &&
                countryEditText.error == null &&
                cityEditText.error == null && districtEditText.error == null &&
                streetEditText.error == null && houseNumberEditText.error == null &&
                flatNumberEditText.error == null && postCodeEditText.error == null){
                DBUtils.addCompany(this,
                    CleaningCompany(NIP = findViewById<EditText>(R.id.editTextCompanyNIP).text.toString(),
                        email = findViewById<EditText>(R.id.editTextCompanyEmail).text.toString(),
                        phone = findViewById<EditText>(R.id.editTextCompanyPhone).text.toString(),
                        country = findViewById<EditText>(R.id.editTextCompanyCountry).text.toString(),
                        city = findViewById<EditText>(R.id.editTextCompanyCity).text.toString(),
                        district = findViewById<EditText>(R.id.editTextCompanyDistrict).text.toString(),
                        street = findViewById<EditText>(R.id.editTextCompanyStreet).text.toString(),
                        houseNumber = findViewById<EditText>(R.id.editTextCompanyHouse).text.toString(),
                        flatNumber = findViewById<EditText>(R.id.editTextCompanyFlat).text.toString(),
                        postCode = findViewById<EditText>(R.id.editTextCompanyPostCode).text.toString()
                    )
                )
                finish()
            } else{
                Toast.makeText(this, "Invalid company data", Toast.LENGTH_SHORT).show()
            }
        }
        val cancelButton = findViewById<Button>(R.id.buttonCompanyCancel)
        cancelButton.setOnClickListener {
            finish()
        }

        val deleteButton = findViewById<Button>(R.id.buttonCompanyDelete)
        if (extras != null) {
            deleteButton.setOnClickListener {
                DBUtils.deleteCompany(this, extras.getString("nip")!!)
                finish()
            }
        }else{
            deleteButton.isVisible = false
        }


    }
}