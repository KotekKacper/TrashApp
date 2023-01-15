package com.example.trashapp.ui.companies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CompaniesViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is companies Fragment"
    }
    val text: LiveData<String> = _text
}