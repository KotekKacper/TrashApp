package com.example.trashapp.classes

data class CleaningCompany(
    var NIP: String,
    var email: String,
    var phone: Int,
    var country: String,
    var city: String,
    var district: String? = null,
    var street: String,
    var flatNumber: String? = null,
    var postCode: String? = null,
)