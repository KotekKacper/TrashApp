package com.example.trashapp.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

class AnyChangeWatcher (private val fieldsEditText: ArrayList<EditText>,
                        private val data: ArrayList<String>,
                        private val button: Button) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable?) {
        fieldsEditText.zip(data).forEach {pair ->
             if (pair.component1().text.toString() != pair.component2()){
                 button.isEnabled = true
                 return
             }
        }
        button.isEnabled = false
    }
}