package com.example.trashapp.classes

data class Group(
    val id: String,
    val name: String,
    val meetingDate: String,
    val meetingLoc: String,
    var users: String? = null
    )
