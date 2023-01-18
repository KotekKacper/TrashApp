package com.example.trashapp.classes

import java.sql.Timestamp

data class Worker (
    var fullname: String,
    var birthDate: Timestamp,
    var jobStartTime: Timestamp,
    var jobEndTime: Timestamp,
    var cleaningCompanyNIP: String,
    var vehicleId: Int
    )