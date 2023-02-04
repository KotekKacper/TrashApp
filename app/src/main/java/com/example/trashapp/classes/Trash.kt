package com.example.trashapp.classes

import android.graphics.drawable.Drawable
import java.time.Instant

data class Trash(
    var id: String? = null,
    var localization: String,
    var creationDate: String,
    var trashSize: String? = null,
    var vehicleId: String? = null,
    var userLoginReport: String? = null,
    var cleaningCrewId: String? = null,
    var userLogin: String? = null,
    var collectionDate: String? = null,
    var images: ArrayList<String>? = null,
    var trashType: String? = null
)

enum class TrashSize(val intValue:Int?) {
    SMALL(0),
    MEDIUM(1),
    BIG(2)
}