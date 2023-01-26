package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class FullnameWatcher (private val fullnameEditText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        if (check.contains(";") ||
            check.contains("\n") ||
            check.contains("\"")) {
            // show an error message
            fullnameEditText.error = "Field can't contain ; \\n or \""
        } else if (check.length > 40) {
            // show an error message
            fullnameEditText.error = "Maximum of 40 characters"
        } else {
            fullnameEditText.error = null
        }
    }
}