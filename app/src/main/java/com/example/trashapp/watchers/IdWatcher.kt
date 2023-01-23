package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class IdWatcher(private val editText: EditText) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        if (!s.toString().matches("-?\\d+(\\.\\d+)?".toRegex())) {
            editText.error = "Only numbers are allowed"
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}