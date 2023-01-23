package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class SizeWatcher(private val editText: EditText) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val size = s.toString().toLowerCase()
        if (!(size == "small" || size == "medium" || size == "big")) {
            editText.error = "Only 'small', 'medium' or 'big' are allowed"
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}