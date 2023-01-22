package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Pattern

class PhoneWatcher(private val phoneEditText: EditText) : TextWatcher {

    private val phoneNumberPattern = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*\$"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        val phoneNumber = editable.toString()
        val pattern = Pattern.compile(phoneNumberPattern)
        val matcher = pattern.matcher(phoneNumber)
        if (!matcher.matches()) {
            phoneEditText.error = "Invalid phone number"
        } else if (check.length > 40) {
            // show an error message
            phoneEditText.error = "Maximum of 40 characters"
        } else {
            phoneEditText.error = null
        }
    }
}