package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DayTimeWatcher (private val editText: EditText) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val text = s.toString()
        if (!text.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$".toRegex())) {
            editText.error = "Invalid time format. Use HH:mm"
        } else {
            editText.error = null
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}