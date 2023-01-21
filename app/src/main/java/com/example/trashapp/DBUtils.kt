package com.example.trashapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.text.format.DateUtils
import android.util.Log
import android.util.Log.ERROR
import com.example.trashapp.classes.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import java.time.Instant
import java.util.*


object DBUtils {

    private fun useSelect(db: SQLiteDatabase, elements: ArrayList<String>,
                  tabName: String, whereString: String = "")
    : HashMap<String, ArrayList<String>>{
        // TODO - change to prevent sql injection

        val elementsString = elements.joinToString(separator = ", ")
        val sqlString = "SELECT ${elementsString} FROM ${tabName} WHERE ${whereString};"
        Log.i("SQLiteCustom : useSelect : sqlString", sqlString)

        val output = HashMap<String, ArrayList<String>>();
        val cursor = db.rawQuery(sqlString, null)
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

    fun getAllActiveTrash(context: Context): ArrayList<OverlayItem>{
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val items = ArrayList<OverlayItem>()
        try {
            //TODO - change to prevent sql injection
            val elements = ArrayList<String>()
            elements.add("id");elements.add("localization");elements.add("trash_size")
            val whereString = "collection_date IS NULL"

            val qResult = useSelect(db, elements, Tab.TRASH, whereString)
            Log.i("SQLiteCustom", qResult.toString())

            if (qResult["id"] != null) {
                for (i in qResult["id"]!!.indices) {
                    val p = qResult["localization"]?.get(i)?.split(",")
                    items.add(
                        OverlayItem(
                            qResult["id"]!![i],
                            qResult["localization"]!![i],
                            qResult["trash_size"].toString(),
                            GeoPoint(p!![0].toDouble(), p[1].toDouble())
                        )
                    )
                    Log.i("SQLiteCustom : useSelect : localization", p[0] + ", " + p[1])
                }
            }
        }
        catch(ex: Exception)
        {
            Log.w("Exception",ex.message.toString())
        }
        finally{
        db.close()
        }
        return items
    }

    fun addTrash(context: Context, pos: GeoPoint, chosen_imgs : ArrayList<Uri>, size: String, user_login_report: String? = null, vehicle_id: Int? = null, user_login: String? = null, crew_id:Int? = null){
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        try {
            //TODO - improve to add element with all given properties and to prevent sql injection
            val position: String = pos.toDoubleString()
            val sqlString: String =
                "INSERT INTO ${Tab.TRASH} (localization, creation_date, trash_size, user_login_report, vehicle_id, user_login,cleaningcrew_id, collection_date) " +
                        "VALUES (?, datetime(), ?, ?,  ${vehicle_id}, '${user_login}', ${crew_id}, NULL)"
            val statement = db.compileStatement(sqlString)
            statement.bindString(1, position)
            statement.bindLong(2, TrashSize.valueOf(size.uppercase()).intValue!!.toLong())
            statement.bindString(3,user_login_report)
            statement.executeInsert()

            //db.execSQL(sqlString)
        }
        catch (ex: Exception)
        {
            Log.w("Exception : ",ex.message.toString())
        }
        finally{
            db.close()
        }
    }

    fun collectTrash(context: Context, item: OverlayItem){
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        //TODO - delete the chosen element from DB

        db.close()
    }

    fun getReports(context: Context, username: String): ArrayList<Trash> {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        //TODO - return trash reported by the user and images connected to it

        db.close()
        return arrayListOf(
            Trash(
                creationDate = Instant.now(),
                localization = "52.40427145950248,16.94963942393314,0.0",
                id = 1,
                trashSize = 1
            )
        )
    }

    fun getGroups(context: Context, username: String): ArrayList<Group> {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        //TODO - return groups that the given user is connected to

        db.close()
        return arrayListOf(
            Group(
                name = "Sprzątacze crew",
                meetingDate = Instant.now(),
                meetingLoc = "52.40427145950248,16.94963942393314,0.0"
            )
        )
    }

    fun getCollectingPoints(context: Context): ArrayList<TrashCollectingPoint> {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        try
        {

        }
        catch(ex:Exception)
        {

        }
        finally{
            db.close()
        }
        //TODO - return all collecting points


        return arrayListOf(
            TrashCollectingPoint(
                localization = "52.40427145950248,16.94963942393314,0.0"
            )
        )
    }

    fun getUsers(context: Context): ArrayList<User> {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        //TODO - return all users

        db.close()
        return arrayListOf(
            User(
                login = "login",
                password = "",
                email = ""
            )
        )
    }

    fun getCompanies(context: Context): ArrayList<CleaningCompany> {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        //TODO - return all companies

        db.close()
        return arrayListOf(
            CleaningCompany(
                email = "contact@company.com",
                NIP = "",
                phone = 0,
                country = "",
                city = "",
                street = ""
            )
        )
    }

    fun addUser(context: Context, user: User): Boolean{
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        //TODO - insert user into database (return true - added, return false - not added)

        db.close()
        return true
    }

    fun checkLogin(context: Context, username: String, password: String): Boolean{
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        // TODO - check if login and password are correct

        db.close()
        return true
    }
}