package com.example.trashapp.classes

data class CleaningCompany(
    var NIP: String,
    var email: String,
    var phone: String,
    var country: String? = null,
    var city: String? = null,
    var district: String? = null,
    var street: String? = null,
    var houseNumber: String? = null,
    var flatNumber: String? = null,
    var postCode: String? = null,
)