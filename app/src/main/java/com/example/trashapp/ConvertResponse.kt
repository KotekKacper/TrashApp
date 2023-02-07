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
                        country = if (attributes[3] == "null") "" else attributes[3],
                        city = if (attributes[4] == "null") "" else attributes[4],
                        district = if (attributes[5] == "null") "" else attributes[5],
                        street = if (attributes[6] == "null") "" else attributes[6],
                        flatNumber = if (attributes[7] == "null") "" else attributes[7],
                        postCode = if (attributes[8] == "null") "" else attributes[8],
                        houseNumber = if (attributes[9] == "null") "" else attributes[9],
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
                        phone = if (attributes[3] == "0") "" else attributes[3],
                        fullname = attributes[4],
                        country = if (attributes[5] == "null") "" else attributes[5],
                        city = if (attributes[6] == "null") "" else attributes[6],
                        district = if (attributes[7] == "null") "" else attributes[7],
                        street = if (attributes[8] == "null") "" else attributes[8],
                        flatNumber = if (attributes[9] == "null") "" else attributes[9],
                        postCode = if (attributes[10] == "null") "" else attributes[10],
                        houseNumber = if (attributes[11] == "null") "" else attributes[11],
                        roles = ArrayList(attributes[12].trimEnd(',').split(","))
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
                                trashType = ArrayList(attributes[4].trimStart(',').trimEnd(',').split(","))
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
                val date = if (attributes[2] == "null") null else attributes[2]
                out.add(
                    Group(
                        id = attributes[0],
                        name = attributes[1],
                        meetingDate = date,
                        meetingLoc = attributes[3],
                        users = attributes[4].trimStart(',').trimEnd(',')
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
                        trashType = attributes[10].trimStart(',').trimEnd(','),
                        images = if (attributes[11].isNotEmpty()) ArrayList(attributes[11].split(",")) else arrayListOf<String>()
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
    fun convertRoles(str: String): ArrayList<Role>{
        val points = str.split("\n")
        val out: ArrayList<Role> = arrayListOf()
        for (point in points){
            if(!point.isEmpty() && point !="\n") {
                val attributes = point.split(";")
                out.add(
                    Role(
                        name = attributes[0]
                    )
                )
            }
        }
        return out
    }
}