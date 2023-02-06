package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class TypesWatcher (private val editText: EditText) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val list = s.toString().split(",")
        if (list.isEmpty()) {
            editText.error = "At least one type"
        } else if (list.any { it.isBlank() } || list.size != list.toSet().size) {
            editText.error = "Type list should not contain empty values or duplicates"
        } else {
            editText.error = null
        }
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}