package com.example.trashapp.ui.vehicles

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.classes.TrashCollectingPoint
import com.example.trashapp.classes.Vehicle
import com.example.trashapp.watchers.BothOrNoneWatcher
import com.example.trashapp.watchers.LatitudeWatcher
import com.example.trashapp.watchers.LongitudeWatcher
import com.example.trashapp.watchers.PercentWatcher

class AddVehicleActivity : AppCompatActivity() {
    private var adding = true
    private var vehicleId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)

        val tx1 = this.findViewById<TextView>(R.id.textVehicleFilling)
        val s1 = SpannableString(tx1.text.toString()+" *")
        s1.setSpan(
            ForegroundColorSpan(Color.RED),
            s1.length-1, s1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx1.text = s1

        val extras = intent.extras;
        if (extras != null) {
            adding = false
            vehicleId = extras.getString("id")!!
            try {
                try {
                    this.findViewById<EditText>(R.id.editTextVehicleLatitude).text =
                        SpannableStringBuilder(extras.getString("latitude"))
                    this.findViewById<EditText>(R.id.editTextVehicleLongitude).text =
                        SpannableStringBuilder(extras.getString("longitude"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                this.findViewById<EditText>(R.id.editTextVehicleFilling).text =
                    SpannableStringBuilder(extras.getDouble("filling").toString())

                if (extras.getBoolean("inUse"))
                    this.findViewById<CheckBox>(R.id.checkboxVehicleInUse).isChecked = true
            } catch (e: Exception) {
                Log.e("IntentExtras", e.toString())
            }
        }

        val latitudeEditText = this.findViewById<EditText>(R.id.editTextVehicleLatitude)
        latitudeEditText.addTextChangedListener(LatitudeWatcher(latitudeEditText, optional = true))

        val longitudeEditText = this.findViewById<EditText>(R.id.editTextVehicleLongitude)
        longitudeEditText.addTextChangedListener(LongitudeWatcher(longitudeEditText, optional = true))

        latitudeEditText.addTextChangedListener(BothOrNoneWatcher(longitudeEditText, latitudeEditText))
        longitudeEditText.addTextChangedListener(BothOrNoneWatcher(longitudeEditText, latitudeEditText))

        val fillingEditText = this.findViewById<EditText>(R.id.editTextVehicleFilling)
        fillingEditText.addTextChangedListener(PercentWatcher(fillingEditText))

        val applyButton = findViewById<Button>(R.id.buttonVehicleConfirm)
        applyButton.setOnClickListener{
            if (longitudeEditText.error == null &&
                latitudeEditText.error == null &&
                fillingEditText.text.toString().isNotEmpty() &&
                fillingEditText.error == null){
                DBUtils.addVehicle(this, adding,
                    Vehicle(localization = arrayListOf(
                            latitudeEditText.text.toString(), longitudeEditText.text.toString())
                            .joinToString(","),
                        inUse = this.findViewById<CheckBox>(R.id.checkboxVehicleInUse).isChecked,
                        filling = fillingEditText.text.toString().toDouble(),
                        id = vehicleId
                    ), vehicleId
                )
            } else{
                Toast.makeText(this, "Invalid vehicle data", Toast.LENGTH_SHORT).show()
            }
        }
        val cancelButton = findViewById<Button>(R.id.buttonVehicleCancel)
        cancelButton.setOnClickListener {
            finish()
        }

        val deleteButton = findViewById<Button>(R.id.buttonVehicleDelete)
        if (extras != null) {
            deleteButton.setOnClickListener {
                DBUtils.deleteVehicle(this, vehicleId)
                finish()
            }
        }else{
            deleteButton.isVisible = false
        }

    }
}