package com.example.trashapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import com.example.trashapp.classes.Tab
import com.example.trashapp.classes.Trash
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import java.time.Instant
import java.util.*

object DBUtils {

    fun useSelect(db: SQLiteDatabase, elements: ArrayList<String>,
                  tabName: String, whereString: String = "")
    : HashMap<String, ArrayList<String>>{
        val elementsString = elements.joinToString(separator = ", ")
        val sqlString = "SELECT ${elementsString} FROM ${tabName} ${whereString};"
        Log.i("SQLiteCustom : useSelect : sqlString", sqlString)

        var output = HashMap<String, ArrayList<String>>();
        val cursor = db?.rawQuery(sqlString, null)
        cursor.use {
            while(it!!.moveToNext()){
                for (element: String in elements){
                    val rowResult = it.getString(it.getColumnIndexOrThrow(element))
                    Log.i("SQLiteCustom", "${element}: ${rowResult}")
                    if(output.containsKey(element)){
                        output[element]?.add(rowResult)
                    }else{
                        output[element] = arrayListOf(rowResult)
                    }
                }
            }
        }
        db.close()
        return output;
    }

    fun getAllActiveTrashFromDB(db: SQLiteDatabase): ArrayList<OverlayItem>{
        //TODO - get all active trash elements from DB
        val elements = ArrayList<String>()
        elements.add("id");elements.add("localization");elements.add("trash_size")
        val qResult = useSelect(db, elements, Tab.TRASH)
        Log.i("SQLiteCustom", qResult.toString())
        var items = ArrayList<OverlayItem>()
        if (qResult["id"] != null){
            for (i in qResult["id"]!!.indices){
                val p = qResult["localization"]?.get(i)?.split(",")
                items.add(OverlayItem(qResult["id"]!![i],
                    qResult["localization"]!![i],
                    qResult["trash_size"].toString(),
                    GeoPoint(p!![0].toDouble(), p[1].toDouble())))
                Log.i("SQLiteCustom : useSelect : localization", p!![0]+", "+p[1])
            }
        }
        return items
    }

    fun addTrashToDB(db: SQLiteDatabase, pos: GeoPoint, chosen_imgs : ArrayList<Uri>, size: String){
        //TODO - add element to DB
        val position: String = pos.toDoubleString()
        val sqlString: String = "INSERT INTO ${Tab.TRASH} (localization, creation_date, trash_size, user_login_report) " +
                                "VALUES ('${position}', datetime(), 1, 'kacper')"
        Log.i("SQLiteCustom : addTrash : sqlStringGenr", sqlString)
        db.execSQL(sqlString)
        db.close()
        Log.i("SQLiteCustom", "Trash added")
    }

    fun delTrashFromDB(db: SQLiteDatabase, item: OverlayItem){
        //TODO - delete the chosen element from DB
    }

    fun getReports(context: Context, username: String): ArrayList<Trash> {
        //TODO - return trash and images connected to it
        return arrayListOf(
            Trash(
                creationDate = Instant.now(),
                localization = "Teatralna 3",
                id = 1,
                trashSize = 1
            )
        )
    }
}