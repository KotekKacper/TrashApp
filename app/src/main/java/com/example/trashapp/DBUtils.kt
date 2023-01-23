package com.example.trashapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.ConvertResponse.convertAllUsers
import com.example.trashapp.ConvertResponse.convertCollectionPoints
import com.example.trashapp.ConvertResponse.convertCompanies
import com.example.trashapp.ConvertResponse.convertUserReports
import com.example.trashapp.adapters.*
import com.example.trashapp.classes.*
import com.example.trashapp.databinding.FragmentAccountBinding
import com.example.trashapp.ui.collectingpoints.AddPointActivity
import com.example.trashapp.ui.companies.AddCompanyActivity
import com.example.trashapp.ui.groups.AddGroupActivity
import com.example.trashapp.ui.reports.AddReportActivity
import com.example.trashapp.ui.users.AddUserActivity
import com.example.trashapp.ui.vehicles.AddVehicleActivity
import com.example.trashapp.ui.workers.AddWorkerActivity
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

    private fun checkForError(context: Context, output: String): Boolean{
        if (output.startsWith("ERROR")){
            Toast.makeText(context, output.split(":")[1], Toast.LENGTH_SHORT).show()
            return true
        }
        return false
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
                    if (checkForError(context, json.toString())){
                        return@withContext
                    }

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
                    if (checkForError(context, json.toString())){
                        return@withContext
                    }

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
                    if (checkForError(context, json.toString())){
                        return@withContext
                    }

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
                        if (checkForError(context, json.toString())){
                            return@withContext
                        }

                        val usersArray = json?.let { convertAllUsers(json.toString()) }
                        val adapter = UserItemAdapter(usersArray, object : OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                val intent = Intent(context, AddUserActivity::class.java)
                                intent.putExtra("login", usersArray?.get(position)?.login)
                                intent.putExtra("password", usersArray?.get(position)?.password)
                                intent.putExtra("email", usersArray?.get(position)?.email)
                                intent.putExtra("fullname", usersArray?.get(position)?.fullname)
                                intent.putExtra("phone", usersArray?.get(position)?.phone)
                                intent.putExtra("country", usersArray?.get(position)?.country)
                                intent.putExtra("city", usersArray?.get(position)?.city)
                                intent.putExtra("district", usersArray?.get(position)?.district)
                                intent.putExtra("street", usersArray?.get(position)?.street)
                                intent.putExtra("houseNumber", usersArray?.get(position)?.houseNumber)
                                intent.putExtra("flatNumber", usersArray?.get(position)?.flatNumber)
                                intent.putExtra("postCode", usersArray?.get(position)?.postCode)
                                context.startActivity(intent)
                            }})
                        recyclerView.adapter = adapter

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("ServerSQL", e.toString())
                    }
                }
            }
        }

    fun addUser(context: Context, user: User){
        //TODO - insert user into database

    }

    fun deleteUser(login: String){

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
                    if (checkForError(context, json.toString())){
                        return@withContext
                    }

                    val companiesArray = json?.let { convertCompanies(json.toString()) }
                    val adapter = CompanyItemAdapter(companiesArray, object : OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, AddCompanyActivity::class.java)
                            intent.putExtra("nip", companiesArray?.get(position)?.NIP)
                            intent.putExtra("email", companiesArray?.get(position)?.email)
                            intent.putExtra("phone", companiesArray?.get(position)?.phone)
                            intent.putExtra("country", companiesArray?.get(position)?.country)
                            intent.putExtra("city", companiesArray?.get(position)?.city)
                            intent.putExtra("district", companiesArray?.get(position)?.district)
                            intent.putExtra("street", companiesArray?.get(position)?.street)
                            intent.putExtra("houseNumber", companiesArray?.get(position)?.houseNumber)
                            intent.putExtra("flatNumber", companiesArray?.get(position)?.flatNumber)
                            intent.putExtra("postCode", companiesArray?.get(position)?.postCode)
                            context.startActivity(intent)
                        }})
                    recyclerView.adapter = adapter

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun addCompany(context: Context, company:CleaningCompany){

    }

    fun deleteCompany(context: Context, nip: String){

    }


    fun getVehicles(context: Context, recyclerView: RecyclerView){
        val vehiclesArray = arrayListOf(Vehicle(
            id = "1",
            inUse = true,
            filling = 0.4,
            localization = "52.40427145950248,16.94963942393314,0.0",
            workers = "Ivan (2023-01-18),Kacper (2001-01-01)"
        ))
        val funSend = "getVehicles"

//        val elements = ArrayList<String>()

//        val dataToSend = elements.joinToString(separator = ", ")

        CoroutineScope(Dispatchers.IO).launch {
            try {
//                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
//                    val json = response.body()?.string()
//                    Log.i("ServerSQL", json.toString())
//                    if (checkForError(context, json.toString())){
//                        return@withContext
//                    }

//                    val usersArray = json?.let { convertAllUsers(json.toString()) }
                    val adapter = VehicleItemAdapter(vehiclesArray, object : OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, AddVehicleActivity::class.java)
                            intent.putExtra("id", vehiclesArray?.get(position)?.id)
                            intent.putExtra("inUse", vehiclesArray?.get(position)?.inUse)
                            intent.putExtra("filling", vehiclesArray?.get(position)?.filling)
                            intent.putExtra("latitude",
                                vehiclesArray[position].localization?.split(",")?.get(0)
                            )
                            intent.putExtra("longitude",
                                vehiclesArray[position].localization?.split(",")?.get(1)
                            )
                            context.startActivity(intent)
                        }})
                    recyclerView.adapter = adapter

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun addVehicle(vehicle: Vehicle){

    }

    fun deleteVehicle(vehicleId: String){

    }




    fun getWorkers(context: Context, recyclerView: RecyclerView){
        val workersArray = arrayListOf(Worker(
            fullname = "Ivan",
            birthDate = "2023-01-18",
            jobStartTime = "8:00",
            jobEndTime = "16:00",
            cleaningCompanyNIP = "3345",
            vehicleId = "1"
        ))

        CoroutineScope(Dispatchers.IO).launch {
            try {
//                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
//                    val json = response.body()?.string()
//                    Log.i("ServerSQL", json.toString())
//                    if (checkForError(context, json.toString())){
//                        return@withContext
//                    }

//                    val usersArray = json?.let { convertAllUsers(json.toString()) }
                    val adapter = WorkerItemAdapter(workersArray, object : OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, AddWorkerActivity::class.java)
                            intent.putExtra("fullname", workersArray?.get(position)?.fullname)
                            intent.putExtra("birthDate", workersArray?.get(position)?.birthDate)
                            intent.putExtra("jobStartTime", workersArray?.get(position)?.jobStartTime)
                            intent.putExtra("jobEndTime", workersArray?.get(position)?.jobEndTime)
                            intent.putExtra("cleaningCompanyNIP", workersArray?.get(position)?.cleaningCompanyNIP)
                            intent.putExtra("vehicleId", workersArray?.get(position)?.vehicleId)
                            context.startActivity(intent)
                        }})
                    recyclerView.adapter = adapter

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }

    }

    fun addWorker(context: Context, worker: Worker){

    }

    fun deleteWorker(context: Context, fullname: String, birthDate: String){

    }






    fun getAccount(context: Context, binding: FragmentAccountBinding, username: String){

        binding.editTextTextAccountLogin.text = SpannableStringBuilder("login")
        binding.editTextTextAccountPassword.text = SpannableStringBuilder("password")
        //...

    }

    fun checkLogin(context: Context, username: String, password: String): Boolean{
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        // TODO - check if login and password are correct

        db.close()
        return true
    }
}