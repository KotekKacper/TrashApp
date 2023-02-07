package com.example.trashapp.classes

data class Group(
    val id: String,
    val name: String,
    val meetingDate: String? = null,
    val meetingLoc: String? = null,
    var users: String? = null
    )
