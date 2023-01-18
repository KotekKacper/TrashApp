package com.example.trashapp.classes

import android.graphics.drawable.Drawable
import java.time.Instant

data class Trash(
    var id: Int,
    var localization: String,
    var creationDate: Instant,
    var trashSize: Int,
    var vehicleId: Int? = null,
    var userLoginReport: String? = null,
    var cleaningCrewId: Int? = null,
    var userLogin: String? = null,
    var collectionDate: Instant? = null,
    var images: ArrayList<Drawable>? = null
)