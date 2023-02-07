package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class BothOrNoneWatcher (private val firstEditText: EditText, private val secondEditText: EditText) : TextWatcher{
    override fun afterTextChanged(s: Editable?) {
        if (firstEditText.text.isNotEmpty() xor secondEditText.text.isNotEmpty()) {
            firstEditText.error = "Both fields need to be populated"
            secondEditText.error = "Both fields need to be populated"
        } else {
            firstEditText.error = null
            secondEditText.error = null
        }
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}