package com.example.trashapp.classes

import java.sql.Timestamp
import java.time.Instant

class Trash {
    var id: Int = 0
    var localization: String = ""
    var creationDate: Timestamp? = null
    var trashSize: Int = 0
    var vehicleId: Int? = null
    var userLoginReport: String? = null
    var cleaningCrewId: Int? = null
    var userLogin: String? = null
    var collectionDate: Timestamp? = null

    constructor(id: Int, localization: String, creationDate: Timestamp, trashSize: Int, vehicleId: Int?, userLoginReport: String?, cleaningCrewId:Int?, userLogin:String?, collectionDate: Timestamp?=null)
    {
        this.id = id
        this.localization = localization
        this.creationDate = creationDate
        this.trashSize = trashSize
        this.vehicleId = vehicleId
        this.userLoginReport = userLoginReport
        this.cleaningCrewId = cleaningCrewId
        this.userLogin = userLogin
        this.collectionDate = collectionDate
    }
    

}