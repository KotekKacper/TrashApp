package com.example.trashapp.ui.workers

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
import com.example.trashapp.classes.Vehicle
import com.example.trashapp.classes.Worker
import com.example.trashapp.watchers.*

class AddWorkerActivity : AppCompatActivity() {
    private var adding = true
    private var fullname = ""
    private var birthDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_worker)

        val tx1 = this.findViewById<TextView>(R.id.textWorkerFullName)
        val s1 = SpannableString(tx1.text.toString()+" *")
        s1.setSpan(
            ForegroundColorSpan(Color.RED),
            s1.length-1, s1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx1.text = s1

        val tx2 = this.findViewById<TextView>(R.id.textWorkerBirthDate)
        val s2 = SpannableString(tx2.text.toString()+" *")
        s2.setSpan(
            ForegroundColorSpan(Color.RED),
            s2.length-1, s2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx2.text = s2

        val tx3 = this.findViewById<TextView>(R.id.textWorkerJobStartTime)
        val s3 = SpannableString(tx3.text.toString()+" *")
        s3.setSpan(
            ForegroundColorSpan(Color.RED),
            s3.length-1, s3.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx3.text = s3

        val tx4 = this.findViewById<TextView>(R.id.textWorkerJobEndTime)
        val s4 = SpannableString(tx4.text.toString()+" *")
        s4.setSpan(
            ForegroundColorSpan(Color.RED),
            s4.length-1, s4.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx4.text = s4

        val tx5 = this.findViewById<TextView>(R.id.textWorkerCompanyNip)
        val s5 = SpannableString(tx5.text.toString()+" *")
        s5.setSpan(
            ForegroundColorSpan(Color.RED),
            s5.length-1, s5.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx5.text = s5

        val tx6 = this.findViewById<TextView>(R.id.textWorkerVehicleId)
        val s6 = SpannableString(tx6.text.toString()+" *")
        s6.setSpan(
            ForegroundColorSpan(Color.RED),
            s6.length-1, s6.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tx6.text = s6





        val extras = intent.extras;
        if (extras != null) {
            adding = false
            try {
                val fullNameEditText = this.findViewById<EditText>(R.id.editTextWorkerFullName)
                fullNameEditText.text = SpannableStringBuilder(extras.getString("fullname"))
                fullname = extras.getString("fullname")!!

                val birthDateEditText = this.findViewById<EditText>(R.id.editTextWorkerBirthDate)
                birthDateEditText.text = SpannableStringBuilder(extras.getString("birthDate"))
                birthDate = extras.getString("birthDate")!!

                val jobStartTimeEditText = this.findViewById<EditText>(R.id.editTextWorkerJobStartTime)
                jobStartTimeEditText.text = SpannableStringBuilder(extras.getString("jobStartTime"))

                val jobEndTimeEditText = this.findViewById<EditText>(R.id.editTextWorkerJobEndTime)
                jobEndTimeEditText.text = SpannableStringBuilder(extras.getString("jobEndTime"))

                val cleaningCompanyNipEditText = this.findViewById<EditText>(R.id.editTextWorkerCompanyNip)
                cleaningCompanyNipEditText.text = SpannableStringBuilder(extras.getString("cleaningCompanyNIP"))

                val vehicleIdEditText = this.findViewById<EditText>(R.id.editTextWorkerVehicleId)
                vehicleIdEditText.text = SpannableStringBuilder(extras.getString("vehicleId"))

            } catch (e: Exception) {
                Log.e("IntentExtras", e.toString())
            }
        }

        val fullNameEditText = this.findViewById<EditText>(R.id.editTextWorkerFullName)
        fullNameEditText.addTextChangedListener(FullnameWatcher(fullNameEditText))

        val birthDateEditText = this.findViewById<EditText>(R.id.editTextWorkerBirthDate)
        birthDateEditText.addTextChangedListener(DayDateWatcher(birthDateEditText))

        val jobStartTimeEditText = this.findViewById<EditText>(R.id.editTextWorkerJobStartTime)
        jobStartTimeEditText.addTextChangedListener(DayTimeWatcher(jobStartTimeEditText))

        val jobEndTimeEditText = this.findViewById<EditText>(R.id.editTextWorkerJobEndTime)
        jobEndTimeEditText.addTextChangedListener(DayTimeWatcher(jobEndTimeEditText))

        val cleaningCompanyNipEditText = this.findViewById<EditText>(R.id.editTextWorkerCompanyNip)
        cleaningCompanyNipEditText.addTextChangedListener(NipWatcher(cleaningCompanyNipEditText))

        val vehicleIdEditText = this.findViewById<EditText>(R.id.editTextWorkerVehicleId)
        vehicleIdEditText.addTextChangedListener(IdWatcher(vehicleIdEditText))

        val applyButton = findViewById<Button>(R.id.buttonWorkerConfirm)
        applyButton.setOnClickListener{
            if (fullNameEditText.text.isNotEmpty() && birthDateEditText.text.isNotEmpty() &&
                jobStartTimeEditText.text.isNotEmpty() && jobEndTimeEditText.text.isNotEmpty() &&
                cleaningCompanyNipEditText.text.isNotEmpty() && vehicleIdEditText.text.isNotEmpty() &&
                fullNameEditText.error == null && birthDateEditText.error == null &&
                jobStartTimeEditText.error == null && jobEndTimeEditText.error == null &&
                cleaningCompanyNipEditText.error == null && vehicleIdEditText.error == null){
                DBUtils.addWorker(this, adding,
                    Worker(
                        fullNameEditText.text.toString(),
                        birthDateEditText.text.toString(),
                        jobStartTimeEditText.text.toString(),
                        jobEndTimeEditText.text.toString(),
                        cleaningCompanyNipEditText.text.toString(),
                        vehicleIdEditText.text.toString()
                    ), fullname, birthDate
                )
            } else{
                Toast.makeText(this, "Invalid group data", Toast.LENGTH_SHORT).show()
            }
        }
        val cancelButton = findViewById<Button>(R.id.buttonWorkerCancel)
        cancelButton.setOnClickListener {
            finish()
        }

        val deleteButton = findViewById<Button>(R.id.buttonWorkerDelete)
        if (extras != null) {
            deleteButton.setOnClickListener {
                DBUtils.deleteWorker(this, fullname, birthDate)
                finish()
            }
        }else{
            deleteButton.isVisible = false
        }



    }
}