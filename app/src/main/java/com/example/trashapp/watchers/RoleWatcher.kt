package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class RoleWatcher (private val editText: EditText, private val validValues: Set<String>) :
    TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val input = s.toString()
        val items = input.split(",")
        if (items.size > items.toSet().size) {
            // set size is always smaller or equal to list size, so this means there are duplicates
            editText.error = "Duplicate items are not allowed"
            return
        }
        for (item in items) {
            if (!validValues.contains(item)) {
                editText.error = "Invalid item: $item"
                return
            }
        }
        editText.error = null
    }

    // other TextWatcher functions are not needed for this use case
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}