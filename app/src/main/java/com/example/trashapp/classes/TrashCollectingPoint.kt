package com.example.trashapp.classes

data class TrashCollectingPoint (
    var localization: String,
    var busEmpty: Boolean? = null,
    var processingType:String? = null,
    var trashType: Array<TrashType>? = null,
    var trash: Array<Trash>? = null
        )