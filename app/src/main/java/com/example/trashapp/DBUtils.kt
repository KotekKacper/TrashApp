package com.example.trashapp

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.ConvertResponse.convertAllUsers
import com.example.trashapp.ConvertResponse.convertCollectionPoints
import com.example.trashapp.ConvertResponse.convertCompanies
import com.example.trashapp.adapters.CollectingPointItemAdapter
import com.example.trashapp.ConvertResponse.convertUserReports
import com.example.trashapp.adapters.CompanyItemAdapter
import com.example.trashapp.adapters.GroupItemAdapter
import com.example.trashapp.adapters.ReportItemAdapter
import com.example.trashapp.adapters.UserItemAdapter
import com.example.trashapp.classes.*
import com.example.trashapp.databinding.FragmentAccountBinding
import com.example.trashapp.ui.collectingpoints.AddPointActivity
import com.example.trashapp.ui.groups.AddGroupActivity
import com.example.trashapp.ui.reports.AddReportActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.ArrayList


object DBUtils {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .build()
    private val service = retrofit.create(ServerApiService::class.java)

    private fun useSelect(db: SQLiteDatabase, elements: ArrayList<String>,
                  tabName: String, whereString: String = "")
    : HashMap<String, ArrayList<String>>{
        // TODO - change to prevent sql injection
        var whereStringComplete = if(whereString.equals("")) whereString else "WHERE ".plus(whereString)

        val elementsString = elements.joinToString(separator = ", ")
        val sqlString = "SELECT ${elementsString} FROM ${tabName} ${whereStringComplete};"
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

    fun getAllActiveTrash(context: Context):ArrayList<OverlayItem>? {
        val funSend = "getAllActiveTrash"
        var items = ArrayList<OverlayItem>()
        var elements = ArrayList<String>()
        elements.add("${Tab.TRASH}.id");elements.add("${Tab.TRASH}.localization");elements.add("${Tab.TRASH}.creation_date");
        elements.add("${Tab.TRASH}.trash_size");elements.add("${Tab.IMAGE}.content")
        var dataToSend = elements.joinToString(separator = ", ")

        val scope = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())

                    val pointsArray = json?.let { ConvertResponse.convertActiveTrashOnMap(json.toString()) }
                    if (pointsArray != null) {
                        items = pointsArray
                    }


                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }

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
            Log.w("addTrash : Exception : ",ex.message.toString())
        }
        finally{
            db.close()
        }
    }

    fun collectTrash(context: Context, item: OverlayItem){
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        //TODO - Update: add collection_date - not delete.

        db.close()
    }

    fun getReports(context: Context, recyclerView: RecyclerView, username: String) {
        val funSend = "getReports"

        var elements = ArrayList<String>()
        elements.add("${Tab.TRASH}.id");elements.add("${Tab.TRASH}.localization");elements.add("${Tab.TRASH}.creation_date");
        elements.add("${Tab.TRASH}.trash_size");elements.add("${Tab.IMAGE}.content")
        val dataToSend = elements.joinToString(separator = ", ")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())

                    val reportsArray = json?.let { convertUserReports(json.toString()) }
                    val adapter = ReportItemAdapter(reportsArray, object : OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, AddReportActivity::class.java)
                            intent.putExtra("id", reportsArray?.get(position)?.id.toString())
                            intent.putExtra("login", reportsArray?.get(position)?.userLoginReport)
                            intent.putExtra("latitude",
                                reportsArray?.get(position)?.localization?.split(",")?.get(0)
                            )
                            intent.putExtra("longitude",
                                reportsArray?.get(position)?.localization?.split(",")?.get(1)
                            )
                            intent.putExtra("reportDate", reportsArray?.get(position)?.creationDate)
                            intent.putExtra("trashSize", reportsArray?.get(position)?.trashSize.toString())
                            intent.putExtra("trashTypes", reportsArray?.get(position)?.trashType?.joinToString(","))
                            intent.putExtra("collectionDate",reportsArray?.get(position)?.collectionDate)
                            intent.putExtra("collectedBy", "crew")
                            intent.putExtra("collectedVal", reportsArray?.get(position)?.cleaningCrewId?.toString())
                            context.startActivity(intent)
                        }
                    })
                    recyclerView.adapter = adapter

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun addReport(trash: Trash){

    }

    fun deleteReport(id: String){

    }


    fun getGroups(context: Context, recyclerView: RecyclerView, username: String){
        val groupsArray = arrayListOf(
            Group(
                name = "Sprzątacze crew",
                meetingDate = "2023-01-18 23:18:13.0",
                meetingLoc = "52.40427145950248,16.94963942393314,0.0"
            )
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
//                val response = service.getJson(username, funSend)
                withContext(Dispatchers.Main) {
//                    val json = response.body()?.string()
//                    Log.i("ServerSQL", json.toString())
//
//                    val companiesArray = json?.let { convertReports(json.toString()) }
                    val adapter = GroupItemAdapter(groupsArray, object : OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, AddGroupActivity::class.java)
                            intent.putExtra("crewName", groupsArray[position].name)
                            intent.putExtra("meetingDate", groupsArray[position].meetingDate)
                            intent.putExtra("latitude", groupsArray[position].meetingLoc.split(",")[0])
                            intent.putExtra("longitude", groupsArray[position].meetingLoc.split(",")[1])
                            context.startActivity(intent)
                        }
                    })
                    recyclerView.adapter = adapter

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun addGroup(group: Group){

    }

    fun deleteGroup(loc: String){

    }

    fun getCollectingPoints(context: Context, recyclerView: RecyclerView) {

        val funSend = "getCollectingPoints"

        var elements = ArrayList<String>()
        elements.add("localization")
        val dataToSend = elements.joinToString(separator = "")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())

                    val pointsArray = json?.let { convertCollectionPoints(json.toString()) }
                    val adapter = CollectingPointItemAdapter(pointsArray, object : OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, AddPointActivity::class.java)
                            intent.putExtra("latitude",
                                pointsArray?.get(position)?.localization?.split(",")?.get(0)
                            )
                            intent.putExtra("longitude",
                                pointsArray?.get(position)?.localization?.split(",")?.get(1)
                            )
                            intent.putExtra("notInUse", pointsArray?.get(position)?.busEmpty)
                            intent.putExtra("processingType", pointsArray?.get(position)?.processingType.toString())
                            intent.putExtra("trashTypes", pointsArray?.get(position)?.trashType?.joinToString(","))
                            intent.putExtra("trashIds", pointsArray?.get(position)?.trashId?.joinToString(","))
                            context.startActivity(intent)
                        }
                    })
                    recyclerView.adapter = adapter

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun addCollectingPoint(point: TrashCollectingPoint){

    }

    fun deleteCollectingPoint(localization: String){

    }

    fun getUsers(context: Context, recyclerView: RecyclerView){
        val funSend = "getUsers"

        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        var items = kotlin.collections.ArrayList<User>()
        //TODO - return all users

        val elements = ArrayList<String>()
        elements.add("${Tab.USER}.login");elements.add("${Tab.USER}.password")
        elements.add("${Tab.USER}.email");elements.add("${Tab.USER}.phone");
        elements.add("${Tab.USER}.fullname");elements.add("${Tab.ROLE}.role_name")

        val dataToSend = elements.joinToString(separator = ", ")


            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = service.getJson(dataToSend, funSend)
                    withContext(Dispatchers.Main) {
                        val json = response.body()?.string()
                        Log.i("ServerSQL", json.toString())

                        val usersArray = json?.let { convertAllUsers(json.toString()) }
                        val adapter = UserItemAdapter(usersArray)
                        recyclerView.adapter = adapter

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("ServerSQL", e.toString())
                    }
                }
            }
        }

    fun getCompanies(context: Context, recyclerView: RecyclerView){
        val funSend = "getCompanies"

        var elements = ArrayList<String>()
        elements.add("nip");elements.add("email");elements.add("phone")
        elements.add("country");elements.add("city");elements.add("street")
        val dataToSend = elements.joinToString(separator = ", ")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())

                    val companiesArray = json?.let { convertCompanies(json.toString()) }
                    val adapter = CompanyItemAdapter(companiesArray)
                    recyclerView.adapter = adapter

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun getAccount(context: Context, binding: FragmentAccountBinding, username: String){

        binding.editTextTextAccountLogin.text = SpannableStringBuilder("login")
        binding.editTextTextAccountPassword.text = SpannableStringBuilder("password")
        //...

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