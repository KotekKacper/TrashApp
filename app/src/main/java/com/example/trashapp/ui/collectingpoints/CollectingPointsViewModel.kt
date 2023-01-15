package com.example.trashapp.ui.collectingpoints
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CollectingPointsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is collecting points Fragment"
    }
    val text: LiveData<String> = _text
}