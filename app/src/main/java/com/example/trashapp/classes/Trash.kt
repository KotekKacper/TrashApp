package com.example.trashapp.classes

import java.sql.Timestamp
import java.time.Instant

data class Trash(
    var id: Int,
    var localization: String,
    var creationDate: Timestamp?,
    var trashSize: Int,
    var vehicleId: Int?,
    var userLoginReport: String?,
    var cleaningCrewId: Int?,
    var userLogin: String?,
    var collectionDate: Timestamp? = null
)