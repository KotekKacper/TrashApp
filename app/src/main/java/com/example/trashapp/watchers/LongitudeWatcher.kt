package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Pattern

class LongitudeWatcher(private val longitudeEditText: EditText) : TextWatcher {

    private val longitudePattern = "-?(180|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        val phoneNumber = editable.toString()
        val pattern = Pattern.compile(longitudePattern)
        val matcher = pattern.matcher(phoneNumber)
        if (!matcher.matches()) {
            longitudeEditText.error = "Invalid longitude"
        } else if (check.length > 40) {
            // show an error message
            longitudeEditText.error = "Maximum of 40 characters"
        } else {
            longitudeEditText.error = null
        }
    }
}