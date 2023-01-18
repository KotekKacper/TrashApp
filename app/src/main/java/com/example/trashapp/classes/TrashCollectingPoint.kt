package com.example.trashapp.classes

data class TrashCollectingPoint (
    var localization: String,
    var busEmpty: Boolean?,
    var processingType:String?,
    var trashType: Array<TrashType>?,
    var trash: Array<Trash>?
        )