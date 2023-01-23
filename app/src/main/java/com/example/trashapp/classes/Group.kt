package com.example.trashapp.classes

import java.time.Instant

data class Group(
    val name: String,
    val meetingDate: String,
    val meetingLoc: String,
    var users: ArrayList<String>? = null
    )
