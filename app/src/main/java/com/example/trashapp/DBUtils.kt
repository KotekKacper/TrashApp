package com.example.trashapp

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
    fun getAllActiveFromDB(): ArrayList<OverlayItem>{

        var connection: Connection? = null
        val connectionString = "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/dblab02_students.cs.put.poznan.pl"
        val connectionProps = Properties()
        connectionProps["user"] = "inf148114"
        connectionProps["password"] = "inf148114"

        try {
            connection = DriverManager.getConnection(connectionString, "inf148114","inf148114")
            println("Połączono z bazą danych")


            //TODO - get all active trash elements from DB
            try{
                val query = connection.prepareStatement("SELECT * FROM TRASH WHERE COLLECTION_DATE IS NULL;")

                // the query is executed and results are fetched
                val result = query.executeQuery()

                // an empty list for holding the results
                var activeTrashList = ArrayList<Trash>()

                while(result.next())
                {
                    val id = result.getInt("ID")
                    val localization = result.getString("LOCALIZATION")
                    val creationDate = result.getTimestamp("CREATION_DATE")
                    val trashSize = result.getInt("TRASH_SIZE")
                    val vehicleId = result.getInt("VEHICLE_ID")
                    val userLoginReport = result.getString("USER_LOGIN_REPORT")
                    val cleaningCrewId = result.getInt("CLEANINGCREW_ID")
                    val userLogin = result.getString("USER_LOGIN")

                    activeTrashList.add(Trash(id,localization,creationDate,trashSize,vehicleId,userLoginReport,cleaningCrewId,userLogin))
                }
                println(activeTrashList[0].localization)
                result.close()

            }
            catch (ex: SQLException){
                System.out.println("Błąd wykonania polecenia: " + ex.message);
            }
        } catch (ex: SQLException) {
            Logger.getLogger("getAllActiveFromDB").log(Level.SEVERE,"Nie udało się połączyć z bazą danych", ex)
            System.exit(-1)
        }
        var items = ArrayList<OverlayItem>()
        items.add(OverlayItem("1", "Trash", "Desc1", GeoPoint(52.40339, 16.95057)))
        items.add(OverlayItem("2", "Trash2", "Desc2", GeoPoint(52.40349, 16.95057)))
        return items
    }

    fun addToDB(pos: GeoPoint, chosen_imgs : ArrayList<Uri>, size: String){
        //TODO - add element to DB
        val position: String = pos.toDoubleString()
    }

    fun delFromDB(item: OverlayItem){
        //TODO - delete the chosen element from DB
    }
}