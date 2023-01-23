package com.example.trashapp.ui.vehicles
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VehicleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is vehicles Fragment"
    }
    val text: LiveData<String> = _text
}