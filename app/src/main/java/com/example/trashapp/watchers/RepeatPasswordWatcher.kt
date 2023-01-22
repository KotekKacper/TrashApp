package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class RepeatPasswordWatcher (private val repeatPasswordEditText: EditText,
                             private val passwordEditText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        if (check.isEmpty()) {
            // show an error message
            repeatPasswordEditText.error = "Field can't be empty"
        } else if (check.contains(";") ||
            check.contains("\n") ||
            check.contains("\"") ||
            check.contains(" ")) {
            // show an error message
            repeatPasswordEditText.error = "Field can't contain ; \\n \" or space"
        } else if (check.length > 40) {
            // show an error message
            repeatPasswordEditText.error = "Maximum of 40 characters"
        } else if (check != passwordEditText.text.toString()) {
            //show an error message
            repeatPasswordEditText.error = "Passwords don't match"
        } else {
            repeatPasswordEditText.error = null
        }
    }
}