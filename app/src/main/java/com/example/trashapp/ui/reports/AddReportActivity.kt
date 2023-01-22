package com.example.trashapp.ui.reports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.trashapp.R

class AddReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report)

        val extras = intent.extras;
        if (extras != null) {
            val text = this.findViewById<TextView>(R.id.textViewAddReports)
            text.text = extras.getInt("id").toString()
            //The key argument here must match that used in the other activity
        }
    }
}