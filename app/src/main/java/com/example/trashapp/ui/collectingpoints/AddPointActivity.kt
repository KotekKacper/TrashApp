package com.example.trashapp.ui.collectingpoints

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.classes.Group
import com.example.trashapp.classes.TrashCollectingPoint
import com.example.trashapp.watchers.*

class AddPointActivity : AppCompatActivity() {

    private var loc : String = ""
    private var adding = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_point)

        val extras = intent.extras;
        if (extras != null) {
            adding = false
            try {
                this.findViewById<EditText>(R.id.editTextPointLatitude).text =
                    SpannableStringBuilder(extras.getString("latitude"))
                this.findViewById<EditText>(R.id.editTextPointLongitude).text =
                    SpannableStringBuilder(extras.getString("longitude"))
                loc = arrayListOf(extras.getString("latitude"),
                                    extras.getString("longitude"))
                                    .joinToString(",")
                try {
                    if (extras.getBoolean("notInUse")){
                        this.findViewById<CheckBox>(R.id.checkboxPointNotInUse).isChecked = true
                    }
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextPointProcessingType).text =
                        SpannableStringBuilder(extras.getString("processingType"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextPointTrashTypes).text =
                        SpannableStringBuilder(extras.getString("trashTypes"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextPointTrashCollectedHere).text =
                        SpannableStringBuilder(extras.getString("trashIds"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }

            } catch (e: Exception) {
                Log.e("IntentExtras", e.toString())
            }
        }

        val latitudeEditText = this.findViewById<EditText>(R.id.editTextPointLatitude)
        latitudeEditText.addTextChangedListener(LatitudeWatcher(latitudeEditText))

        val longitudeEditText = this.findViewById<EditText>(R.id.editTextPointLongitude)
        longitudeEditText.addTextChangedListener(LongitudeWatcher(longitudeEditText))

        val processingTypeEditText = this.findViewById<EditText>(R.id.editTextPointProcessingType)
        processingTypeEditText.addTextChangedListener(LoginWatcher(processingTypeEditText))

        val trashTypesEditText = this.findViewById<EditText>(R.id.editTextPointTrashTypes)
        trashTypesEditText.addTextChangedListener(ListWatcher(trashTypesEditText))

        val trashHereEditText = this.findViewById<EditText>(R.id.editTextPointTrashCollectedHere)
        trashHereEditText.addTextChangedListener(ListWatcher(trashHereEditText))

        val applyButton = findViewById<Button>(R.id.buttonPointConfirm)
        applyButton.setOnClickListener{
            if (latitudeEditText.error == null && latitudeEditText.text.toString() != "" &&
                longitudeEditText.error == null && longitudeEditText.text.toString() != "" &&
                    processingTypeEditText.error == null && processingTypeEditText.text.toString() != "" &&
                trashTypesEditText.error == null && trashTypesEditText.text.toString() != "" &&
                    trashHereEditText.error == null){
                DBUtils.addCollectingPoint(this, adding,
                    TrashCollectingPoint(localization = arrayListOf(
                            latitudeEditText.text.toString(), longitudeEditText.text.toString())
                            .joinToString(","),
                        busEmpty = this.findViewById<CheckBox>(R.id.checkboxPointNotInUse).isChecked,
                        processingType = processingTypeEditText.text.toString(),
                        trashType = ArrayList(trashTypesEditText.text.toString().split(",")),
                        trashId = ArrayList(trashHereEditText.text.toString().split(","))
                    )
                )
                finish()
            } else{
                Toast.makeText(this, "Invalid collecting point data", Toast.LENGTH_SHORT).show()
            }
        }
        val cancelButton = findViewById<Button>(R.id.buttonPointCancel)
        cancelButton.setOnClickListener {
            finish()
        }

        val deleteButton = findViewById<Button>(R.id.buttonPointDelete)
        if (extras != null) {
            deleteButton.setOnClickListener {
                DBUtils.deleteCollectingPoint(this, loc)
                finish()
            }
        }else{
            deleteButton.isVisible = false
        }

    }
}