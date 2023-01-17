package com.example.trashapp

import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.example.trashapp.classes.Trash
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import java.sql.Connection
import java.sql.DriverManager
import java.sql.DriverManager.println
import java.sql.SQLException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

object DBUtils {

    fun getAllActiveFromDB(db: SQLiteDatabase): ArrayList<OverlayItem>{
        //TODO - get all active trash elements from DB

        var items = ArrayList<OverlayItem>()
        items.add(OverlayItem("1", "Trash", "Desc1", GeoPoint(52.40339, 16.95057)))
        items.add(OverlayItem("2", "Trash2", "Desc2", GeoPoint(52.40349, 16.95057)))
        return items
    }

    fun addToDB(db: SQLiteDatabase, pos: GeoPoint, chosen_imgs : ArrayList<Uri>, size: String){
        //TODO - add element to DB
        val position: String = pos.toDoubleString()
    }

    fun delFromDB(db: SQLiteDatabase, item: OverlayItem){
        //TODO - delete the chosen element from DB
    }
}