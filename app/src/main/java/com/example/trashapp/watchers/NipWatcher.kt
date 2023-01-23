package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class NipWatcher (val editText: EditText) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val input = s.toString()
        if (!input.matches("\\d{10}".toRegex())) {
            editText.error = "Enter a 10-digit numeric code"
        } else {
            editText.error = null
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}