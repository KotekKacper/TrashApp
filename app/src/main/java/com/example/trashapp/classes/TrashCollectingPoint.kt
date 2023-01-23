package com.example.trashapp.classes

data class TrashCollectingPoint(
    var localization: String,
    var busEmpty: Boolean? = null,
    var processingType:String? = null,
    var trashType: ArrayList<String>? = null,
    var trashId: List<String>? = null
        )