package com.example.trashapp

import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import java.util.*

object DBUtils {

    fun getAllActiveTrashFromDB(db: SQLiteDatabase): ArrayList<OverlayItem>{
        //TODO - get all active trash elements from DB
        val cursor = db?.rawQuery("SELECT * FROM Trash", null)
        Log.i("SQLiteCustom", cursor!!.getColumnName(1))
        Log.i("SQLiteCustom", cursor!!.getColumnName(2))
        Log.i("SQLiteCustom", cursor!!.getColumnName(3))
        Log.i("SQLiteCustom", cursor!!.getColumnName(4))
        Log.i("SQLiteCustom", cursor!!.getColumnName(5))
        cursor.use {
            while(it!!.moveToNext()){
                val loc = it.getString(it.getColumnIndexOrThrow("localization"))
                Log.i("SQLiteCustom", "Localization: $loc")
            }
        }

        var items = ArrayList<OverlayItem>()
        items.add(OverlayItem("1", "Trash", "Desc1", GeoPoint(52.40339, 16.95057)))
        items.add(OverlayItem("2", "Trash2", "Desc2", GeoPoint(52.40349, 16.95057)))
        return items
    }

    fun addTrashToDB(db: SQLiteDatabase, pos: GeoPoint, chosen_imgs : ArrayList<Uri>, size: String){
        //TODO - add element to DB
        val position: String = pos.toDoubleString()

    }

    fun delTrashFromDB(db: SQLiteDatabase, item: OverlayItem){
        //TODO - delete the chosen element from DB
    }
}