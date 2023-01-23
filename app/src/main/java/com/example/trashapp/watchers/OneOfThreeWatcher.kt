package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

class OneOfThreeWatcher(
    private val firstEditText: EditText,
    private val secondEditText: EditText,
    private val thirdEditText: EditText
) : TextWatcher {

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (firstEditText.isFocused && firstEditText.text.isNotEmpty()) {
            secondEditText.isEnabled = false
            thirdEditText.isEnabled = false
        } else if (secondEditText.isFocused && secondEditText.text.isNotEmpty()) {
            firstEditText.isEnabled = false
            thirdEditText.isEnabled = false
        } else if (thirdEditText.isFocused && thirdEditText.text.isNotEmpty()) {
            firstEditText.isEnabled = false
            secondEditText.isEnabled = false
        }
    }

    override fun afterTextChanged(s: Editable?) {
        if (firstEditText.text.isBlank() && secondEditText.text.isBlank() && thirdEditText.text.isBlank()) {
            firstEditText.isEnabled = true
            secondEditText.isEnabled = true
            thirdEditText.isEnabled = true
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}