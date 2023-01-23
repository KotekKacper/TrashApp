package com.example.trashapp.classes

import java.sql.Timestamp

data class Worker (
    var fullname: String,
    var birthDate: String,
    var jobStartTime: String,
    var jobEndTime: String,
    var cleaningCompanyNIP: String,
    var vehicleId: String
    )