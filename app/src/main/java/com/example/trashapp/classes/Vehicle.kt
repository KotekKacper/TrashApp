package com.example.trashapp.classes

data class Vehicle(
    var id: String,
    var inUse: Boolean,
    var localization: String? = null,
    var filling: Double,
    var workers: String? = null
)