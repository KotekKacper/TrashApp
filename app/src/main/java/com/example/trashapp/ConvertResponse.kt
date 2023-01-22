package com.example.trashapp

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.example.trashapp.classes.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object ConvertResponse {
    var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
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
                        phone = attributes[2].toInt(),
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
                        roles = Role(attributes[5])
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
                        localization = attributes[0]
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
                val drawableArray = ArrayList<Drawable>()
                drawableArray.add(drawableImage)
                out.add(
                    Trash(
                        id = attributes[0].toInt(),
                        localization = attributes[1],
                        creationDate = Instant.parse(attributes[2]),
                        trashSize = attributes[3].toInt(),
                        images = drawableArray
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
                val drawableArray = ArrayList<Drawable>()
                val imageString = attributes[5]
                if(!imageString.equals("null")) {
                    val imageBytes = imageString.toByteArray()
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    val drawableImage = BitmapDrawable(bitmap)
                    drawableArray.add(drawableImage)
                }
                out.add(
                    Trash(
                        id = attributes[0].toInt(),
                        localization = attributes[1],
                        creationDate = LocalDateTime.parse(attributes[2], formatter).toInstant(ZoneOffset.UTC),

                        trashSize = attributes[3].toInt(),
                        collectionDate = Instant.parse(attributes[4]),
                        images = drawableArray
                    )
                )
            }
        }
        return out
    }
}