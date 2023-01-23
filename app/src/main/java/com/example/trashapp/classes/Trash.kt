package com.example.trashapp.classes

import android.graphics.drawable.Drawable
import java.time.Instant

data class Trash(
    var id: String? = null,
    var localization: String,
    var creationDate: String,
    var trashSize: Int? = null,
    var vehicleId: Int? = null,
    var userLoginReport: String? = null,
    var cleaningCrewId: Int? = null,
    var userLogin: String? = null,
    var collectionDate: String? = null,
    var images: ArrayList<Drawable>? = null,
    var trashType: ArrayList<String>? = null
)

enum class TrashSize(val intValue:Int?) {
    SMALL(0),
    MEDIUM(1),
    BIG(2)
}