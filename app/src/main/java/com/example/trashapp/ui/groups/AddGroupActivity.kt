package com.example.trashapp.ui.groups

import android.content.Context
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
import com.example.trashapp.watchers.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AddGroupActivity : AppCompatActivity() {
    private var adding = true
    private var id = ""
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val zoneId = ZoneId.systemDefault()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        val tx1 = this.findViewById<TextView>(R.id.textGroupCrewName)
        val s1 = SpannableString(tx1.text.toString()+" *")
        s1.setSpan(
            ForegroundColorSpan(Color.RED),
            s1.length-1, s1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx1.text = s1


        val meetDatePicker = findViewById<DatePicker>(R.id.datePickerGroupMeetDate)
        val meetTimePicker = findViewById<TimePicker>(R.id.timePickerGroupMeetDate)
        meetTimePicker.setIs24HourView(true)
        val checkbox= findViewById<CheckBox>(R.id.checkboxMeetingDate)
        checkbox.setOnCheckedChangeListener { _, isChecked -> run {
            meetDatePicker.isEnabled = isChecked
            meetTimePicker.isEnabled = isChecked
        }
        }
        checkbox.isChecked = false
        meetDatePicker.isEnabled = checkbox.isChecked
        meetTimePicker.isEnabled = checkbox.isChecked

        val extras = intent.extras;
        if (extras != null) {
            adding = false
            id = extras.getString("id")!!
            try {
                this.findViewById<EditText>(R.id.editTextGroupCrewName).text =
                    SpannableStringBuilder(extras.getString("crewName"))
                try {
                    val curDate = LocalDateTime.parse(extras.getString("meetingDate"), formatter)
                    this.findViewById<DatePicker>(R.id.datePickerGroupMeetDate)
                        .updateDate(curDate.year, curDate.monthValue-1, curDate.dayOfMonth)
                    Log.i("Date", curDate.toString())
                    val creationTimePicker = this.findViewById<TimePicker>(R.id.timePickerGroupMeetDate)
                    creationTimePicker.hour = curDate.hour
                    creationTimePicker.minute = curDate.minute
                    checkbox.isChecked = true
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                this.findViewById<EditText>(R.id.editTextGroupLatitude).text =
                    SpannableStringBuilder(extras.getString("latitude"))
                this.findViewById<EditText>(R.id.editTextGroupLongitude).text =
                    SpannableStringBuilder(extras.getString("longitude"))
                this.findViewById<EditText>(R.id.editTextTextGroupMembers).text =
                    SpannableStringBuilder(extras.getString("groupMembers"))

            } catch (e: Exception) {
            Log.e("IntentExtras", e.toString())
            }
        }

        val crewNameEditText = this.findViewById<EditText>(R.id.editTextGroupCrewName)
        crewNameEditText.addTextChangedListener(LoginWatcher(crewNameEditText))


        val latitudeEditText = this.findViewById<EditText>(R.id.editTextGroupLatitude)
        latitudeEditText.addTextChangedListener(LatitudeWatcher(latitudeEditText, true))

        val longitudeEditText = this.findViewById<EditText>(R.id.editTextGroupLongitude)
        longitudeEditText.addTextChangedListener(LongitudeWatcher(longitudeEditText, true))

        val membersEditText = this.findViewById<EditText>(R.id.editTextTextGroupMembers)
        membersEditText.addTextChangedListener(LoginListWatcher(membersEditText))

        val applyButton = findViewById<Button>(R.id.buttonGroupConfirm)
        applyButton.setOnClickListener{
            if (crewNameEditText.error == null && crewNameEditText.text.toString() != "" &&
                latitudeEditText.error == null && longitudeEditText.error == null){
                var meetTime: String? = null
                if (checkbox.isChecked){
                    meetTime = ZonedDateTime.of(meetDatePicker.year,
                        meetDatePicker.month+1,
                        meetDatePicker.dayOfMonth,
                        meetTimePicker.hour,
                        meetTimePicker.minute,
                        0, 0, zoneId).format(formatter)
                }
                DBUtils.addGroup(this, adding,
                    Group(
                        id = "-1", name = crewNameEditText.text.toString(),
                        meetingDate = meetTime,
                        meetingLoc = arrayListOf(
                            latitudeEditText.text.toString(), longitudeEditText.text.toString()).joinToString(","),
                        users = membersEditText.text.toString().trimEnd(',')
                    ), id
                )
            } else{
                Toast.makeText(this, "Invalid group data", Toast.LENGTH_SHORT).show()
            }
        }
        val cancelButton = findViewById<Button>(R.id.buttonGroupCancel)
        cancelButton.setOnClickListener {
            finish()
        }

        val deleteButton = findViewById<Button>(R.id.buttonGroupDelete)
        if (extras != null) {
            deleteButton.setOnClickListener {
                DBUtils.deleteGroup(this, id)
                finish()
            }
        }else{
            deleteButton.isVisible = false
        }


    }
}