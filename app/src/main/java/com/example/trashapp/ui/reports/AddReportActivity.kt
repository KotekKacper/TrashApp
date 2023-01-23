package com.example.trashapp.ui.reports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.example.trashapp.R
import com.example.trashapp.watchers.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report)

        val extras = intent.extras;
        if (extras != null) {
            try{
                this.findViewById<EditText>(R.id.editTextTextReportLoginReported).text =
                    SpannableStringBuilder(extras.getString("login"))
                this.findViewById<EditText>(R.id.editTextTextReportLocalizationLat).text =
                    SpannableStringBuilder(extras.getString("latitude"))
                this.findViewById<EditText>(R.id.editTextTextReportLocalizationLon).text =
                    SpannableStringBuilder(extras.getString("longitude"))
                this.findViewById<EditText>(R.id.editTextTextReportCreationDate).text =
                    SpannableStringBuilder(extras.getString("reportDate"))
                if (extras.getString("trashTypes") != null){
                    this.findViewById<EditText>(R.id.editTextTextReportTrashType).text =
                        SpannableStringBuilder(extras.getString("trashTypes"))
                }
                if (extras.getString("collectionDate") != null){
                    this.findViewById<EditText>(R.id.editTextTextReportCollectionDate).text =
                        SpannableStringBuilder(extras.getString("collectionDate"))
                    when(extras.getString("collectedBy")){
                        "user" -> this.findViewById<EditText>(R.id.editTextTextReportLoginCollected).text =
                            SpannableStringBuilder(extras.getString("collectedVal"))
                        "crew" -> this.findViewById<EditText>(R.id.editTextTextReportCrewCollected).text =
                            SpannableStringBuilder(extras.getString("collectedVal"))
                        "vehicle" -> this.findViewById<EditText>(R.id.editTextTextReportVehicleCollected).text =
                            SpannableStringBuilder(extras.getString("collectedVal"))
                    }
                }
            } catch (e: Exception) {
                Log.e("IntentExtras", e.toString())
            }


            val loginReportedEditText = this.findViewById<EditText>(R.id.editTextTextReportLoginReported)
            val loginReportedWatcher = LoginWatcher(loginReportedEditText)
            loginReportedEditText.addTextChangedListener(loginReportedWatcher)

            val latitudeEditText = this.findViewById<EditText>(R.id.editTextTextReportLocalizationLat)
            val latitudeWatcher = LatitudeWatcher(latitudeEditText)
            latitudeEditText.addTextChangedListener(latitudeWatcher)

            val longitudeEditText = this.findViewById<EditText>(R.id.editTextTextReportLocalizationLon)
            val longitudeWatcher = LongitudeWatcher(longitudeEditText)
            longitudeEditText.addTextChangedListener(longitudeWatcher)

            val creationDateEditText = this.findViewById<EditText>(R.id.editTextTextReportCreationDate)
            val creationDateWatcher = DateWatcher(creationDateEditText)
            creationDateEditText.addTextChangedListener(creationDateWatcher)

            val trashTypeEditText = this.findViewById<EditText>(R.id.editTextTextReportTrashType)
            val trashTypeWatcher = LoginWatcher(trashTypeEditText)
            trashTypeEditText.addTextChangedListener(trashTypeWatcher)

            val collectionDateEditText = this.findViewById<EditText>(R.id.editTextTextReportCollectionDate)
            val collectionDateWatcher = DateWatcher(collectionDateEditText)
            collectionDateEditText.addTextChangedListener(collectionDateWatcher)

            val firstEditText = findViewById<EditText>(R.id.editTextTextReportLoginCollected)
            firstEditText.addTextChangedListener(LoginWatcher(firstEditText))
            val secondEditText = findViewById<EditText>(R.id.editTextTextReportVehicleCollected)
            secondEditText.addTextChangedListener(IdWatcher(secondEditText))
            val thirdEditText = findViewById<EditText>(R.id.editTextTextReportCrewCollected)
            thirdEditText.addTextChangedListener(IdWatcher(thirdEditText))

            firstEditText.addTextChangedListener(OneOfThreeWatcher(firstEditText, secondEditText, thirdEditText))
            secondEditText.addTextChangedListener(OneOfThreeWatcher(firstEditText, secondEditText, thirdEditText))
            thirdEditText.addTextChangedListener(OneOfThreeWatcher(firstEditText, secondEditText, thirdEditText))


        }
    }
}