package com.example.trashapp

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.example.trashapp.classes.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object ConvertResponse {
    var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")

    fun convertToSize(str: String): String{
        return when(str){
            "0" -> "Small"
            "1" -> "Medium"
            else -> "Big"
        }
    }

    fun convertFromSize(str: String): String{
        return when(str){
            "Small" -> "0"
            "Medium" -> "1"
            else -> "2"
        }
    }

    fun convertCompanies(str: String) : ArrayList<CleaningCompany> {
        val companies = str.split("\n")
        val out: ArrayList<CleaningCompany> = arrayListOf()
        for (company in companies){
            if(!company.isEmpty()) {
                val attributes = company.split(";")
                out.add(
                    CleaningCompany(
                        NIP = attributes[0],
                        email = attributes[1],
                        phone = attributes[2],
                        country = attributes[3],
                        city = attributes[4],
                        street = attributes[5]
                    )
                )
            }
        }
        return out
    }
    fun convertAllUsers(str: String) : ArrayList<User> {
        val users = str.split("\n")
        val out: ArrayList<User> = arrayListOf()
        for (user in users){
            if(!user.isEmpty()) {
                val attributes = user.split(";")
                out.add(
                    User(
                        login = attributes[0],
                        password = attributes[1],
                        email = attributes[2],
                        phone = attributes[3],
                        fullname = attributes[4],
                        country = attributes[5],
                        city = attributes[6],
                        street = attributes[7],
                        postCode = attributes[8],
                        roles = arrayListOf(attributes[9])
                    )
                )
            }
        }
        return out
    }
    fun convertCollectionPoints(str: String) : ArrayList<TrashCollectingPoint> {
        val points = str.split("\n")
        val out: ArrayList<TrashCollectingPoint> = arrayListOf()
        for (point in points){
            if(!point.isEmpty()) {
                val attributes = point.split(";")
                out.add(
                    TrashCollectingPoint(
                        localization = attributes[0],
                                busEmpty = attributes[1] == "1",
                                processingType = attributes[2],
                                trashId = attributes[3].split("-"),
                                trashType = ArrayList(attributes[4].split(","))
                    )
                )
            }
        }
        return out
    }
    fun convertGroups(str: String) : ArrayList<Group> {
        val points = str.split("|")
        val out: ArrayList<Group> = arrayListOf()
        for (point in points){
            if(!point.isEmpty() && point !="|") {
                val attributes = point.split(";")
                out.add(
                    Group(
                        id = attributes[0],
                        name = attributes[1],
                        meetingDate = attributes[2],
                        meetingLoc = arrayListOf(attributes[3], attributes[4])
                            .joinToString(","),
                        users = attributes[5]
                    )
                )
            }
        }
        return out
    }
    fun convertActiveTrash(str: String) : ArrayList<Trash> {
        val points = str.split("\n")
        val out: ArrayList<Trash> = arrayListOf()
        for (point in points){
            if(!point.isEmpty()) {
                val attributes = point.split(";")

                val imageString = attributes[4]
                val imageBytes = imageString.toByteArray()
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                val drawableImage = BitmapDrawable(bitmap)
                out.add(
                    Trash(
                        id = attributes[0],
                        localization = attributes[1],
                        creationDate = attributes[2],
                        trashSize = convertToSize(attributes[3])
                    )
                )
            }
        }
        return out
    }

    fun convertActiveTrashOnMap(str: String) : ArrayList<OverlayItem> {
        val points = str.split("\n")
        val out = ArrayList<OverlayItem>()
        for (point in points){
            if(!point.isEmpty()) {
                val attributes = point.split(";")
                val drawableArray = ArrayList<Drawable>()
                val imageString = attributes[4]
                if(!imageString.equals("null")) {
                    val imageBytes = imageString.toByteArray()
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    val drawableImage = BitmapDrawable(bitmap)
                    drawableArray.add(drawableImage)
                }
                var id = attributes[0]
                var localization = attributes[1]
                var creationDate = attributes[2]
                val localDateTime: LocalDateTime = LocalDateTime.parse(creationDate, formatter)
                creationDate = localDateTime.toInstant(ZoneOffset.UTC).toString();
                var trashSize = attributes[3]
                var images = drawableArray
                val p = localization?.split(",")
                out.add(
                    OverlayItem(
                        id,
                        localization,
                        trashSize,
                        GeoPoint(p!![0].toDouble(), p[1].toDouble())
                    )
                )
            }
        }
        return out
    }
    fun convertUserReports(str: String) : ArrayList<Trash> {
        val points = str.split("\n")
        val out: ArrayList<Trash> = arrayListOf()
        for (point in points){
            if(!point.isEmpty()) {
                val attributes = point.split(";")
                out.add(
                    Trash(
                        id = attributes[0],
                        localization = attributes[1],
                        creationDate = attributes[2],
                        trashSize = convertToSize(attributes[3]),
                        collectionDate = attributes[4],
                        userLoginReport =  attributes[5],
                        userLogin = attributes[6],
                        vehicleId = attributes[7],
                        cleaningCrewId = attributes[8],
                        collectingPoint = attributes[9],
                        trashType = attributes[10],
                        images = arrayListOf()
                    )
                )
            }
        }
        return out
    }
    fun convertVehicles(str: String): ArrayList<Vehicle>{
        val points = str.split("\n")
        val out: ArrayList<Vehicle> = arrayListOf()
        for (point in points){
            if(!point.isEmpty() && point !="\n") {
                val attributes = point.split(";")
                var inUse = false
                if (attributes[1] == 'T'.toString()){
                    inUse = true
                }
                out.add(
                    Vehicle(
                        id = attributes[0],
                        inUse = inUse,
                        localization = attributes[2],
                        filling = attributes[3].toDouble()
                    )
                )
            }
        }
        return out
    }
    fun convertWorkers(str: String): ArrayList<Worker>{
        val points = str.split("\n")
        val out: ArrayList<Worker> = arrayListOf()
        for (point in points){
            if(!point.isEmpty() && point !="\n") {
                val attributes = point.split(";")
                out.add(
                    Worker(
                        fullname = attributes[0],
                        birthDate = attributes[1].split(" ")[0],
                        jobStartTime = attributes[2],
                        jobEndTime = attributes[3],
                        cleaningCompanyNIP = attributes[4],
                        vehicleId = attributes[5]
                    )
                )
            }
        }
        return out
    }
}