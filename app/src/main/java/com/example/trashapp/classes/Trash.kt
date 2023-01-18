package com.example.trashapp.classes

import java.sql.Timestamp
import java.time.Instant

data class Trash(
    var id: Int,
    var localization: String,
    var creationDate: Timestamp?,
    var trashSize: TrashSize,
    var vehicleId: Int?,
    var userLoginReport: String?,
    var cleaningCrewId: Int?,
    var userLogin: String?,
    var collectionDate: Timestamp? = null,
    var trashType: TrashType? = null
)

enum class TrashSize(val intValue:Int?) {
    SMALL(0),
    MEDIUM(1),
    BIG(2)
}