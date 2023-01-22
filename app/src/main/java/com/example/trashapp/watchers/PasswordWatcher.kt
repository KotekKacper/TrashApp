package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PasswordWatcher (private val passwordEditText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        if (check.isEmpty()) {
            // show an error message
            passwordEditText.error = "Field can't be empty"
        } else if (check.contains(";") ||
            check.contains("\n") ||
            check.contains("\"") ||
            check.contains(" ")) {
            // show an error message
            passwordEditText.error = "Field can't contain ; \\n  \" or space"
        } else {
            passwordEditText.error = null
        }
    }
}