package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Pattern

class PostCodeWatcher(private val postCodeEditText: EditText) : TextWatcher {

    private val postCodePattern = "^\\d+(-\\d+)*\$"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        val phoneNumber = editable.toString()
        val pattern = Pattern.compile(postCodePattern)
        val matcher = pattern.matcher(phoneNumber)
        if (check.isEmpty()) {
            postCodeEditText.error = null
        } else if (!matcher.matches()) {
            postCodeEditText.error = "Invalid post code"
        } else if (check.length > 40) {
            // show an error message
            postCodeEditText.error = "Maximum of 40 characters"
        } else {
            postCodeEditText.error = null
        }
    }
}