package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class EmailWatcher(private val emailEditText: EditText) : TextWatcher {
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {
        val check = editable.toString()
        if (check.count { it == '@' } != 1) {
            //show an error message
            emailEditText.error = "Email must contain exactly one @ sign"
        } else if (check.contains(";") ||
            check.contains("\n") ||
            check.contains("\"") ||
            check.contains(" ")) {
            // show an error message
            emailEditText.error = "Field can't contain ; \\n  \" or space"
        } else if (check.length > 40) {
            // show an error message
            emailEditText.error = "Maximum of 40 characters"
        } else if (check.indexOf("@") == 0 || check.indexOf("@") == check.length - 1) {
            //show an error message
            emailEditText.error = "Email must contain something before and after @"
        } else {
            emailEditText.error = null
        }
    }
}