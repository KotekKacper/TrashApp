package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Pattern

class LatitudeWatcher(private val latitudeEditText: EditText) : TextWatcher {

    private val latitudePattern = "-?(90|([1-8]?\\d)(\\.\\d+)?)"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        val phoneNumber = editable.toString()
        val pattern = Pattern.compile(latitudePattern)
        val matcher = pattern.matcher(phoneNumber)
        if (!matcher.matches()) {
            latitudeEditText.error = "Invalid latitude"
        } else if (check.length > 40) {
            // show an error message
            latitudeEditText.error = "Maximum of 40 characters"
        } else {
            latitudeEditText.error = null
        }
    }
}