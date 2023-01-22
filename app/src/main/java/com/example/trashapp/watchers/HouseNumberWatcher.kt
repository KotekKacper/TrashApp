package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Pattern

class HouseNumberWatcher(private val houseNumberEditText: EditText) : TextWatcher {

    private val houseNumberPattern = "^\\d+[a-zA-Z]*\\b"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        val phoneNumber = editable.toString()
        val pattern = Pattern.compile(houseNumberPattern)
        val matcher = pattern.matcher(phoneNumber)
        if (!matcher.matches()) {
            houseNumberEditText.error = "Invalid house number"
        } else if (check.length > 40) {
            // show an error message
            houseNumberEditText.error = "Maximum of 40 characters"
        } else {
            houseNumberEditText.error = null
        }
    }
}