package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CountryWatcher(private val countryEditText: EditText) : TextWatcher {

    private val allowedCharacters = "qwertyuiopasdfghjklzxcvbnm" +
                                    "QWERTYUIOPASDFGHJKLZXCVBNM" +
                                    "ęóąśłżźćńĘÓĄŚŁŻŹĆŃ -"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        for (element in check) {
            if (!allowedCharacters.contains(element)) {
                countryEditText.error = "Only letters, space and - are allowed"
                return
            } else if (check.length > 40) {
                // show an error message
                countryEditText.error = "Maximum of 40 characters"
                return
            }
        }
        countryEditText.error = null
    }
}