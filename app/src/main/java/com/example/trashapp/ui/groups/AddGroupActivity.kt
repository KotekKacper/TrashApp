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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.classes.Group
import com.example.trashapp.watchers.DateWatcher
import com.example.trashapp.watchers.LatitudeWatcher
import com.example.trashapp.watchers.LoginWatcher
import com.example.trashapp.watchers.LongitudeWatcher

class AddGroupActivity : AppCompatActivity() {
    private var adding = true
    private var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        val tx1 = this.findViewById<TextView>(R.id.textGroupCrewName)
        val s1 = SpannableString(tx1.text.toString()+" *")
        s1.setSpan(
            ForegroundColorSpan(Color.RED),
            s1.length-1, s1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx1.text = s1



        val extras = intent.extras;
        if (extras != null) {
            adding = false
            id = extras.getString("id")!!
            try {
                this.findViewById<EditText>(R.id.editTextGroupCrewName).text =
                    SpannableStringBuilder(extras.getString("crewName"))
                try {
                    this.findViewById<EditText>(R.id.editTextGroupMeetingDate).text =
                        SpannableStringBuilder(extras.getString("meetingDate"))
                } catch (e: Exception) {
                    Log.e("IntentExtras", e.toString())
                }
                this.findViewById<EditText>(R.id.editTextGroupLatitude).text =
                    SpannableStringBuilder(extras.getString("latitude"))
                this.findViewById<EditText>(R.id.editTextGroupLongitude).text =
                    SpannableStringBuilder(extras.getString("longitude"))

            } catch (e: Exception) {
            Log.e("IntentExtras", e.toString())
            }
        }

        val crewNameEditText = this.findViewById<EditText>(R.id.editTextGroupCrewName)
        crewNameEditText.addTextChangedListener(LoginWatcher(crewNameEditText))

        val meetingDateEditText = this.findViewById<EditText>(R.id.editTextGroupMeetingDate)
        meetingDateEditText.addTextChangedListener(DateWatcher(meetingDateEditText))

        val latitudeEditText = this.findViewById<EditText>(R.id.editTextGroupLatitude)
        latitudeEditText.addTextChangedListener(LatitudeWatcher(latitudeEditText))

        val longitudeEditText = this.findViewById<EditText>(R.id.editTextGroupLongitude)
        longitudeEditText.addTextChangedListener(LongitudeWatcher(longitudeEditText))


        val applyButton = findViewById<Button>(R.id.buttonGroupConfirm)
        applyButton.setOnClickListener{
            if (crewNameEditText.error == null && crewNameEditText.text.toString() != "" &&
                meetingDateEditText.error == null &&
                latitudeEditText.error == null && longitudeEditText.error == null){
                DBUtils.addGroup(this, adding,
                    Group(
                        id = "-1", name = crewNameEditText.text.toString(),
                        meetingDate = meetingDateEditText.text.toString(),
                        meetingLoc = arrayListOf(
                            latitudeEditText.text.toString(), longitudeEditText.text.toString()).joinToString(",")
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