package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class LoginWatcher (private val loginEditText: EditText, private val obligatory: Boolean = true) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable?) {
            val check = editable.toString()
            if (check.isEmpty() && obligatory) {
                // show an error message
                loginEditText.error = "Field can't be empty"
            } else if (check.contains(";") ||
                       check.contains("\n") ||
                       check.contains("\"") ||
                        check.contains("`") ||
                        check.contains("|") ||
                        check.contains(" ")) {
                // show an error message
                loginEditText.error = "Field can't contain ; \\n \" ` | or space"
            } else if (check.length > 40) {
                // show an error message
                loginEditText.error = "Maximum of 40 characters"
            } else {
                loginEditText.error = null
            }
        }
}