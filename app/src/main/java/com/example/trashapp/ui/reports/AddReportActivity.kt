package com.example.trashapp.ui.reports

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.adapters.ImageItemAdapter
import com.example.trashapp.classes.Trash
import com.example.trashapp.watchers.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddReportActivity : AppCompatActivity() {
    private var adding = true
    private var id = ""
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
    private val zoneId = ZoneId.systemDefault()
    private var imageIDs = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report)

        val tx1 = this.findViewById<TextView>(R.id.textReportLoginReported)
        val s1 = SpannableString(tx1.text.toString()+" *")
        s1.setSpan(
            ForegroundColorSpan(Color.RED),
            s1.length-1, s1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx1.text = s1

        val tx2 = this.findViewById<TextView>(R.id.textReportLocalization)
        val s2 = SpannableString(tx2.text.toString()+" *")
        s2.setSpan(
            ForegroundColorSpan(Color.RED),
            s2.length-1, s2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx2.text = s2

        val tx3 = this.findViewById<TextView>(R.id.textReportCreationDate)
        val s3 = SpannableString(tx3.text.toString()+" *")
        s3.setSpan(
            ForegroundColorSpan(Color.RED),
            s3.length-1, s3.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx3.text = s3



        val extras = intent.extras;
        if (extras != null) {
            adding = false
            id = extras.getString("id")!!
            try {
                this.findViewById<EditText>(R.id.editTextTextReportLoginReported).text =
                    SpannableStringBuilder(extras.getString("login"))
                this.findViewById<EditText>(R.id.editTextTextReportLocalizationLat).text =
                    SpannableStringBuilder(extras.getString("latitude"))
                this.findViewById<EditText>(R.id.editTextTextReportLocalizationLon).text =
                    SpannableStringBuilder(extras.getString("longitude"))
                val curDate = LocalDateTime.parse(extras.getString("reportDate"), formatter)
                this.findViewById<DatePicker>(R.id.datePickerReportCreationDate)
                    .updateDate(curDate.year, curDate.monthValue, curDate.dayOfMonth)
                val creationTimePicker = this.findViewById<TimePicker>(R.id.timePickerReportCreationDate)
                creationTimePicker.hour = curDate.hour
                creationTimePicker.minute = curDate.minute

                try {
                    this.findViewById<Spinner>(R.id.spinnerReportTrashSize).setSelection(
                        when (extras.getString("trashSize")) {
                            "Small" -> 0
                            "Medium" -> 1
                            else -> 2
                        }
                    )
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    this.findViewById<EditText>(R.id.editTextTextReportTrashType).text =
                        SpannableStringBuilder(extras.getString("trashTypes"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    if (extras.getString("collectionDate") != null) {
                        val colDate =
                            LocalDateTime.parse(extras.getString("collectionDate"), formatter)
                        this.findViewById<DatePicker>(R.id.datePickerReportCollectionDate)
                            .updateDate(colDate.year, colDate.monthValue, colDate.dayOfMonth)
                        val collectionTimePicker =
                            this.findViewById<TimePicker>(R.id.timePickerReportCollectionDate)
                        collectionTimePicker.hour = colDate.hour
                        collectionTimePicker.minute = colDate.minute
                        when (extras.getString("collectedBy")) {
                            "user" -> this.findViewById<EditText>(R.id.editTextTextReportLoginCollected).text =
                                SpannableStringBuilder(extras.getString("collectedVal"))
                            "crew" -> this.findViewById<EditText>(R.id.editTextTextReportCrewCollected).text =
                                SpannableStringBuilder(extras.getString("collectedVal"))
                            "vehicle" -> this.findViewById<EditText>(R.id.editTextTextReportVehicleCollected).text =
                                SpannableStringBuilder(extras.getString("collectedVal"))
                        }
                    }
                }  catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                try {
                    imageIDs = ArrayList(extras.getString("images")!!.split(","))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
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

        val creationDatePicker = findViewById<DatePicker>(R.id.datePickerReportCreationDate)
        val creationTimePicker = findViewById<TimePicker>(R.id.timePickerReportCreationDate)
        creationTimePicker.setIs24HourView(true)

//        val trashSizeEditText = this.findViewById<EditText>(R.id.editTextTextReportTrashSize)
//        val trashSizeWatcher = SizeWatcher(trashSizeEditText)
//        trashSizeEditText.addTextChangedListener(trashSizeWatcher)
        val trashSizeSpinner = this.findViewById<Spinner>(R.id.spinnerReportTrashSize)

        val trashTypeEditText = this.findViewById<EditText>(R.id.editTextTextReportTrashType)
        val trashTypeWatcher = ListWatcher(trashTypeEditText)
        trashTypeEditText.addTextChangedListener(trashTypeWatcher)

        val collectionDatePicker = findViewById<DatePicker>(R.id.datePickerReportCollectionDate)
        val collectionTimePicker = findViewById<TimePicker>(R.id.timePickerReportCollectionDate)
        collectionTimePicker.setIs24HourView(true)


        val firstEditText = findViewById<EditText>(R.id.editTextTextReportLoginCollected)
        firstEditText.addTextChangedListener(LoginWatcher(firstEditText, obligatory = false))
        val secondEditText = findViewById<EditText>(R.id.editTextTextReportVehicleCollected)
        secondEditText.addTextChangedListener(IdWatcher(secondEditText))
        val thirdEditText = findViewById<EditText>(R.id.editTextTextReportCrewCollected)
        thirdEditText.addTextChangedListener(IdWatcher(thirdEditText))

        firstEditText.addTextChangedListener(OneOfThreeWatcher(firstEditText, secondEditText, thirdEditText))
        secondEditText.addTextChangedListener(OneOfThreeWatcher(firstEditText, secondEditText, thirdEditText))
        thirdEditText.addTextChangedListener(OneOfThreeWatcher(firstEditText, secondEditText, thirdEditText))


        if (imageIDs.size > 0){
            Log.e("ImageIDs", imageIDs.joinToString(","))
            val recyclerView = this.findViewById<RecyclerView>(R.id.recyclerViewImages)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = ImageItemAdapter(imageIDs)
            recyclerView.adapter = adapter
        }


        val applyButton = findViewById<Button>(R.id.buttonReportConfirm)
        applyButton.setOnClickListener{
            if (loginReportedEditText.error == null && loginReportedEditText.text.toString() != "" &&
                    latitudeEditText.error == null && latitudeEditText.text.toString() != "" &&
                    longitudeEditText.error == null && longitudeEditText.text.toString() != "" &&
//                        creationDateEditText.error == null && creationDateEditText.text.toString() != "" &&
//                    trashSizeEditText.error == null && trashTypeEditText.error == null &&
//                        collectionDateEditText.error == null && firstEditText.error == null &&
                    secondEditText.error == null && thirdEditText.error == null){
                val crTime = ZonedDateTime.of(creationDatePicker.year,
                                                     creationDatePicker.month+1,
                                                     creationDatePicker.dayOfMonth,
                                                     creationTimePicker.hour,
                                                     creationTimePicker.minute,
                                                     0, 0, zoneId)
                val colTime = ZonedDateTime.of(collectionDatePicker.year,
                    collectionDatePicker.month+1,
                    collectionDatePicker.dayOfMonth,
                    collectionTimePicker.hour,
                    collectionTimePicker.minute,
                    0, 0, zoneId)
                DBUtils.addReport(this, adding,
                    Trash(
                        userLoginReport = loginReportedEditText.text.toString(),
                        localization = arrayListOf(
                            latitudeEditText.text.toString(), longitudeEditText.text.toString())
                            .joinToString(","),
                        creationDate = crTime.format(formatter),
                        trashSize = trashSizeSpinner.selectedItem.toString(),
                        trashType = (trashTypeEditText.text.toString()),
                        collectionDate = colTime.format(formatter)
                ), id)
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
                DBUtils.deleteReport(this, id)
                finish()
            }
        }else{
            deleteButton.isVisible = false
        }
    }
}