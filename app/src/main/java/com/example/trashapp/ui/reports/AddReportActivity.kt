package com.example.trashapp.ui.reports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.classes.Trash
import com.example.trashapp.watchers.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddReportActivity : AppCompatActivity() {
    private var adding = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report)

        val extras = intent.extras;
        if (extras != null) {
            adding = false
            try {
                this.findViewById<EditText>(R.id.editTextTextReportLoginReported).text =
                    SpannableStringBuilder(extras.getString("login"))
                this.findViewById<EditText>(R.id.editTextTextReportLocalizationLat).text =
                    SpannableStringBuilder(extras.getString("latitude"))
                this.findViewById<EditText>(R.id.editTextTextReportLocalizationLon).text =
                    SpannableStringBuilder(extras.getString("longitude"))
                this.findViewById<EditText>(R.id.editTextTextReportCreationDate).text =
                    SpannableStringBuilder(extras.getString("reportDate"))
                try {
                    this.findViewById<EditText>(R.id.editTextTextReportTrashSize).text =
                        when (extras.getString("trashSize")) {
                            "1" -> SpannableStringBuilder("small")
                            "2" -> SpannableStringBuilder("medium")
                            else -> SpannableStringBuilder("big")
                        }
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextTextReportTrashType).text =
                        SpannableStringBuilder(extras.getString("trashTypes"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                if (extras.getString("collectionDate") != null) {
                    this.findViewById<EditText>(R.id.editTextTextReportCollectionDate).text =
                        SpannableStringBuilder(extras.getString("collectionDate"))
                    when (extras.getString("collectedBy")) {
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

            val trashSizeEditText = this.findViewById<EditText>(R.id.editTextTextReportTrashSize)
            val trashSizeWatcher = SizeWatcher(trashSizeEditText)
            trashSizeEditText.addTextChangedListener(trashSizeWatcher)

            val trashTypeEditText = this.findViewById<EditText>(R.id.editTextTextReportTrashType)
            val trashTypeWatcher = ListWatcher(trashTypeEditText)
            trashTypeEditText.addTextChangedListener(trashTypeWatcher)

            val collectionDateEditText = this.findViewById<EditText>(R.id.editTextTextReportCollectionDate)
            val collectionDateWatcher = DateWatcher(collectionDateEditText)
            collectionDateEditText.addTextChangedListener(collectionDateWatcher)

            val firstEditText = findViewById<EditText>(R.id.editTextTextReportLoginCollected)
            firstEditText.addTextChangedListener(LoginWatcher(firstEditText, obligatory = false))
            val secondEditText = findViewById<EditText>(R.id.editTextTextReportVehicleCollected)
            secondEditText.addTextChangedListener(IdWatcher(secondEditText))
            val thirdEditText = findViewById<EditText>(R.id.editTextTextReportCrewCollected)
            thirdEditText.addTextChangedListener(IdWatcher(thirdEditText))

            firstEditText.addTextChangedListener(OneOfThreeWatcher(firstEditText, secondEditText, thirdEditText))
            secondEditText.addTextChangedListener(OneOfThreeWatcher(firstEditText, secondEditText, thirdEditText))
            thirdEditText.addTextChangedListener(OneOfThreeWatcher(firstEditText, secondEditText, thirdEditText))

            val applyButton = findViewById<Button>(R.id.buttonReportConfirm)
            applyButton.setOnClickListener{
                if (loginReportedEditText.error == null && loginReportedEditText.text.toString() != "" &&
                        latitudeEditText.error == null && latitudeEditText.text.toString() != "" &&
                        longitudeEditText.error == null && longitudeEditText.text.toString() != "" &&
                        creationDateEditText.error == null && creationDateEditText.text.toString() != "" &&
                        trashSizeEditText.error == null && trashTypeEditText.error == null &&
                        collectionDateEditText.error == null && firstEditText.error == null &&
                        secondEditText.error == null && thirdEditText.error == null){
                    DBUtils.addReport(this, adding,
                        Trash(localization = arrayListOf(
                        latitudeEditText.text.toString(), longitudeEditText.text.toString()).joinToString(","),
                        creationDate = creationDateEditText.text.toString(),
                        userLoginReport = loginReportedEditText.text.toString(),
                        trashType = ArrayList(trashTypeEditText.text.toString().split(",")),
                        collectionDate = collectionDateEditText.text.toString(),
                    ))
                    finish()
                } else{
                    Toast.makeText(this, "Invalid report data", Toast.LENGTH_SHORT).show()
                }
            }
            val cancelButton = findViewById<Button>(R.id.buttonReportCancel)
            cancelButton.setOnClickListener {
                finish()
            }

            val deleteButton = findViewById<Button>(R.id.buttonReportDelete)
        if (extras != null) {
            deleteButton.setOnClickListener {
                DBUtils.deleteReport(this, extras.getString("id")!!)
                finish()
            }
        }else{
            deleteButton.isVisible = false
        }
    }
}