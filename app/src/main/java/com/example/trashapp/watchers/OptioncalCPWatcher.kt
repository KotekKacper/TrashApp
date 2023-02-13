package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class OptioncalCPWatcher (private val editTextA: EditText, private val Lat: EditText, private val Lon: EditText) :
    TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val areEmpty = arrayListOf(editTextA.text.isNotEmpty())
        if (areEmpty.filter { it == true }.count() != 1) {
            Lat.isEnabled = false
            Lon.isEnabled = false
        } else {
            Lat.isEnabled = true
            Lon.isEnabled = true
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}