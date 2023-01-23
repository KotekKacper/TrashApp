package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DayDateWatcher(private val editText: EditText) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        val datePattern = "^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$"
        val date = s.toString()
        if (!date.matches(datePattern.toRegex())) {
            editText.error = "Invalid date format"
        } else {
            editText.error = null
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}