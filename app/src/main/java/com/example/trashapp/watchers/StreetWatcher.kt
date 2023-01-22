package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class StreetWatcher(private val streetEditText: EditText) : TextWatcher {

    private val allowedCharacters = "qwertyuiopasdfghjklzxcvbnm" +
                                    "QWERTYUIOPASDFGHJKLZXCVBNM" +
                                    "ęóąśłżźćńĘÓĄŚŁŻŹĆŃ -"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        for (element in check) {
            if (!allowedCharacters.contains(element)) {
                streetEditText.error = "Only letters, space and - are allowed"
                return
            } else if (check.length > 40) {
                // show an error message
                streetEditText.error = "Maximum of 40 characters"
                return
            }
        }
        streetEditText.error = null
    }
}