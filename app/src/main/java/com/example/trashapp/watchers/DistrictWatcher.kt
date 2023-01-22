package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DistrictWatcher(private val districtEditText: EditText) : TextWatcher {

    private val allowedCharacters = "qwertyuiopasdfghjklzxcvbnm" +
            "QWERTYUIOPASDFGHJKLZXCVBNM" +
            "ęóąśłżźćńĘÓĄŚŁŻŹĆŃ -"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable?) {
        val check = editable.toString()
        for (element in check) {
            if (!allowedCharacters.contains(element)) {
                districtEditText.error = "Only letters, space and - are allowed"
                return
            } else if (check.length > 40) {
                // show an error message
                districtEditText.error = "Maximum of 40 characters"
                return
            }
        }
        districtEditText.error = null
    }
}