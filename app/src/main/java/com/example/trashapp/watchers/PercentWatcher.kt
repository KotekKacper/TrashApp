package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PercentWatcher(private val editText: EditText) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val input = s.toString()
        if (input.isNotEmpty() && input.toFloat() !in 0f..1f) {
            editText.error = "Value should be between 0 and 1"
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}