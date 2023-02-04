package com.example.trashapp.ui.collectingpoints

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
import com.example.trashapp.classes.Group
import com.example.trashapp.classes.TrashCollectingPoint
import com.example.trashapp.watchers.*

class AddPointActivity : AppCompatActivity() {

    private var loc : String = ""
    private var adding = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_point)

        val tx1 = this.findViewById<TextView>(R.id.textPointLocalization)
        val s1 = SpannableString(tx1.text.toString()+" *")
        s1.setSpan(ForegroundColorSpan(Color.RED),
              s1.length-1, s1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx1.text = s1

        val tx2 = this.findViewById<TextView>(R.id.textPointProcessingType)
        val s2 = SpannableString(tx2.text.toString()+" *")
        s2.setSpan(ForegroundColorSpan(Color.RED),
            s2.length-1, s2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx2.text = s2

        val tx3 = this.findViewById<TextView>(R.id.textPointTrashTypes)
        val s3 = SpannableString(tx3.text.toString()+" *")
        s3.setSpan(ForegroundColorSpan(Color.RED),
            s3.length-1, s3.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx3.text = s3

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
                    if (extras.getString("trashIds") != "null"){
                        this.findViewById<EditText>(R.id.editTextPointTrashCollectedHere).text =
                            SpannableStringBuilder(extras.getString("trashIds"))
                    } else{
                        this.findViewById<EditText>(R.id.editTextPointTrashCollectedHere).text =
                            SpannableStringBuilder("No trash in here")
                    }
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
        trashHereEditText.isEnabled = false
//        trashHereEditText.addTextChangedListener(ListWatcher(trashHereEditText))

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
                    ), loc
                )
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