package com.example.trashapp

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem

object DBUtils {
    fun getAllActiveFromDB(): ArrayList<OverlayItem>{
        //TODO - get all active trash elements from DB
        var items = ArrayList<OverlayItem>()
        items.add(OverlayItem("1", "Trash", "Desc1", GeoPoint(52.40339, 16.95057)))
        items.add(OverlayItem("2", "Trash2", "Desc2", GeoPoint(52.40349, 16.95057)))
        return items
    }

    fun addToDB(pos: GeoPoint, ){

    }

    fun delFromDB(item: OverlayItem){
        //TODO - delete the chosen element from DB
    }
}