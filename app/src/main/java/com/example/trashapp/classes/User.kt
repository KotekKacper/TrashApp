package com.example.trashapp.classes

data class User (
    var login: String,
    var password: String,
    var email: String,
    var phone: String? = null,
    var fullname: String? = null,
    var country: String? = null,
    var city: String? = null,
    var district: String? = null,
    var street: String? = null,
    var flatNumber: String? = null,
    var postCode: String? = null,
    var roles: Role
        )