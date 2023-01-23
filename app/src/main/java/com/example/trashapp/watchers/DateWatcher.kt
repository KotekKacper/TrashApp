package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Pattern

class DateWatcher(private val dateEditText: EditText) : TextWatcher {

    private val datePattern = "(\\d{4})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (00|[0-9]|1[0-9]|2[0-3]):([0-9]|[0-5][0-9]):([0-9]|[0-5][0-9]).\\d"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        val phoneNumber = editable.toString()
        val pattern = Pattern.compile(datePattern)
        val matcher = pattern.matcher(phoneNumber)
        if (!matcher.matches()) {
            dateEditText.error = "Invalid date"
        } else if (check.length > 40) {
            // show an error message
            dateEditText.error = "Maximum of 40 characters"
        } else {
            dateEditText.error = null
        }
    }
}