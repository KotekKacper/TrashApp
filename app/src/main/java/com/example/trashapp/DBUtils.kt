package com.example.trashapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.ConvertResponse.convertAllUsers
import com.example.trashapp.ConvertResponse.convertCollectionPoints
import com.example.trashapp.ConvertResponse.convertCompanies
import com.example.trashapp.ConvertResponse.convertGroups
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
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import retrofit2.Retrofit
import java.time.Clock
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


object DBUtils {

    public val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.11:8888/")
        .build()
    public val service = retrofit.create(ServerApiService::class.java)

    fun checkForError(context: Context, output: String): Boolean{
        if (output.startsWith("ERROR")){
            Toast.makeText(context, output.split(":")[1], Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    fun getAllActiveTrash(context: Context, map: MapView):ArrayList<OverlayItem>? {
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

                        addIconsToMap(context, map, pointsArray, ArrayList() )

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

    fun getAllCollectedTrash(context: Context, map: MapView):ArrayList<OverlayItem>? {
        val funSend = "getAllCollectedTrash"
        var items = ArrayList<OverlayItem>()
        var elements = ArrayList<String>()
        elements.add("${Tab.TRASH}.id");elements.add("${Tab.TRASH}.localization");elements.add("${Tab.TRASH}.creation_date");
        elements.add("${Tab.TRASH}.trash_size")
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

                        addCollectedIconsToMap(context, map, pointsArray)
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
        val funSend = "addTrash"
        var dataToSend = "'${pos.toDoubleString()}', '${user_login_report}', '${TrashSize.valueOf(size.uppercase()).intValue}'"
//        for(img in chosen_imgs) {
//            val dbHelper = DatabaseHelper(context)
//            val db = dbHelper.writableDatabase
//            try {
//                //TODO - improve to add element with all given properties and to prevent sql injection
//                //dataToSend = dataToSend.plus("|${context.contentResolver.openInputStream(img)?.readBytes().toString()}")
//                val sqlString: String =
//                    "INSERT INTO ${Tab.IMAGE} (content, mime_type, trash_id) " + "VALUES (${context.contentResolver.openInputStream(img)?.readBytes()},'png', '${pos.toDoubleString()}')"
//                val statement = db.compileStatement(sqlString)
//                statement.executeInsert()
//                db.close()
//
//            } catch (ex: Exception) {
//                Log.w("addTrash : Exception : ", ex.message.toString())
//            }
//        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())){
                        return@withContext
                    }

                    Toast.makeText(context, "Thank You for report!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun collectTrash(context: Context, item: OverlayItem){
        val funSend = "updateTrash"

        var dataToSend = "collection_date = NOW() | ${Tab.TRASH}.localization = '${item.point.toString()}'"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())){
                        return@withContext
                    }
                    Toast.makeText(context, "Thank You for collecting this trash!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun getReports(context: Context, recyclerView: RecyclerView, username: String) {
        val funSend = "getReports"

        var elements = ArrayList<String>()
        elements.add("${Tab.TRASH}.id");elements.add("${Tab.TRASH}.localization");elements.add("${Tab.TRASH}.creation_date");
        elements.add("${Tab.TRASH}.trash_size");elements.add("${Tab.TRASH}.collection_date");elements.add("${Tab.TRASH}.user_login_report")
        val dataToSend = elements.joinToString(separator = ", ").plus("|${username}")

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
                            intent.putExtra("trashTypes", reportsArray?.get(position)?.trashType?.toString())
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

    fun addReport(context: Context, adding: Boolean, trash: Trash, id: String = ""){
        if(adding) {
            val funSend = "addTrash"
            var dataToSend =
                "'${trash.localization}', '${trash.userLoginReport}', '${trash.trashSize}'"
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = service.getJson(dataToSend, funSend)
                    withContext(Dispatchers.Main) {
                        val json = response.body()?.string()
                        Log.i("ServerSQL", json.toString())
                        if (checkForError(context, json.toString())) {
                            return@withContext
                        }

                        Toast.makeText(context, "Report was added!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("ServerSQL", e.toString())
                    }
                }
            }
        }
    }

    fun deleteReport(context: Context, id: String){
        val funSend = "deleteReport"

        var dataToSend = "id = '${id}'"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    Toast.makeText(context, "Report ${id} was deleted.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun getGroups(context: Context, recyclerView: RecyclerView, username: String){
//        val groupsArray = arrayListOf(
//            Group(
//                id = "1",
//                name = "Sprzątacze crew",
//                meetingDate = "2023-01-18 23:18:13.0",
//                meetingLoc = "52.40427145950248,16.94963942393314,0.0"
//            )
//        )
        val funSend = "getAllGroups"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(username, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
//
                    val groupsArray = json?.let { convertGroups(json.toString()) }
                    val adapter = GroupItemAdapter(groupsArray, object : OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, AddGroupActivity::class.java)
                            intent.putExtra("id", groupsArray?.get(position)?.id)
                            intent.putExtra("crewName", groupsArray?.get(position)?.name)
                            intent.putExtra("meetingDate", groupsArray?.get(position)?.meetingDate)
                            intent.putExtra("latitude",
                                groupsArray?.get(position)?.meetingLoc?.split(",")?.get(0)
                            )
                            intent.putExtra("longitude",
                                groupsArray?.get(position)?.meetingLoc?.split(",")?.get(1)
                            )
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

    fun addGroup(context: Context, adding: Boolean, group: Group, id: String = ""){
        var funSend = ""
        if(adding) {
             funSend = "addGroup"
        }
        else  funSend = "updateGroup"
            val elements = ArrayList<String>()
            elements.add("${Tab.CLEAN_CREW}.crew_name");elements.add("${Tab.CLEAN_CREW}.meet_date")
            elements.add("${Tab.CLEAN_CREW}.meeting_localization")
            var dataToSend = elements.joinToString(separator = ", ")
            dataToSend = dataToSend.plus("|")
            dataToSend = dataToSend.plus("'${group.name}', '${group.meetingDate}', '${group.meetingLoc}'")
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = service.getJson(dataToSend, funSend)
                    withContext(Dispatchers.Main) {
                        val json = response.body()?.string()
                        Log.i("ServerSQL", json.toString())
                        if (checkForError(context, json.toString())) {
                            return@withContext
                        }

                        Toast.makeText(context, "Action was done successfully!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("ServerSQL", e.toString())
                    }
                }

        }
    }

    fun deleteGroup(context: Context, id: String){
        val funSend = "deleteGroup"

        var dataToSend = "id = '${id}'"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    Toast.makeText(context, "Group ${id} was deleted.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun getCollectingPoints(context: Context, recyclerView: RecyclerView) {

        val funSend = "getCollectingPoints"

        var elements = ArrayList<String>()
        elements.add("${Tab.TRASH_COLLECT_POINT}.localization");elements.add("${Tab.TRASH_COLLECT_POINT}.bus_empty");elements.add("${Tab.TRASH_COLLECT_POINT}.processing_type")
        val dataToSend = elements.joinToString(separator = ", ").plus(", GROUP_CONCAT(${Tab.TRASH}.id SEPARATOR '-')")

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

    fun addCollectingPoint(context: Context, adding: Boolean, point: TrashCollectingPoint, localization: String = ""){
        var funSend = "addCollectingPoint"
        if(adding) {
            funSend = "addCollectingPoint"}
        else funSend = "updateCollectingPoint"
            val elements = ArrayList<String>()
            elements.add("${Tab.TRASH_COLLECT_POINT}.localization");elements.add("${Tab.TRASH_COLLECT_POINT}.busEmpty")
            elements.add("${Tab.TRASH_COLLECT_POINT}.processingType");elements.add("${Tab.TRASH_COLLECT_POINT}.trashType")
            elements.add("${Tab.TRASH_COLLECT_POINT}.trashId")
            var dataToSend = elements.joinToString(separator = ", ")
            dataToSend = dataToSend.plus("|")
            dataToSend = dataToSend.plus("'${point.localization}', '${point.busEmpty}', '${point.processingType}' - '${point.trashType}', '${point.trashId}'")
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = service.getJson(dataToSend, funSend)
                    withContext(Dispatchers.Main) {
                        val json = response.body()?.string()
                        Log.i("ServerSQL", json.toString())
                        if (checkForError(context, json.toString())) {
                            return@withContext
                        }

                        Toast.makeText(context, "Action was done successfully!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("ServerSQL", e.toString())
                    }

            }
        }
    }

    fun deleteCollectingPoint(context: Context, localization: String){
        val funSend = "deleteCollectingPoint"

        var dataToSend = "localization = '${localization}'"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    Toast.makeText(context, "Collecting Point ${localization} was deleted.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun getUsers(context: Context, recyclerView: RecyclerView){
        val funSend = "getUsers"

        val elements = ArrayList<String>()
        elements.add("${Tab.USER}.login");elements.add("${Tab.USER}.password")
        elements.add("${Tab.USER}.email");elements.add("${Tab.USER}.phone");
        elements.add("${Tab.USER}.fullname");elements.add("${Tab.USER}.country");
        elements.add("${Tab.USER}.city");elements.add("${Tab.USER}.street");
        elements.add("${Tab.USER}.post_code");elements.add("${Tab.ROLE}.role_name")

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
                                intent.putExtra("roles",
                                    usersArray?.get(position)?.roles?.joinToString(",")
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

    fun addUserRegister(context: Context, adding: Boolean, user: User, login: String = "") {
        var funSend = "addUserRegister"
        var dataToSend = ""


            val elements = ArrayList<String>()
            elements.add("${Tab.USER}.login");elements.add("${Tab.USER}.password")
            elements.add("${Tab.USER}.email");dataToSend = elements.joinToString(separator = ", ")
            dataToSend = dataToSend.plus("|")
            dataToSend = dataToSend.plus("'${user.login}', '${user.password}', '${user.email}'")


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }

    }

    fun addUser(context: Context, adding: Boolean, user: User, login: String = "") {
        var funSend = "addUser"
        var dataToSend = ""
        if(adding)
        {    funSend = "addUser"

        val elements = ArrayList<String>()
            elements.add("${Tab.USER}.login");elements.add("${Tab.USER}.password")
            elements.add("${Tab.USER}.email");elements.add("${Tab.USER}.phone");elements.add("${Tab.USER}.fullname");
            elements.add("${Tab.USER}.country");elements.add("${Tab.USER}.city");elements.add("${Tab.USER}.district");
            elements.add("${Tab.USER}.street");elements.add("${Tab.USER}.flat_number");elements.add("${Tab.USER}.post_code");
        dataToSend = dataToSend.plus("|")
        dataToSend = dataToSend.plus("'${user.login}', '${user.password}', '${user.email}', '${user.phone}', '${user.fullname}', '${user.country}', '${user.city}', '${user.district}', '${user.street}', '${user.flatNumber}', '${user.postCode}'")
        }
        else
        {
            funSend = "updateUser"
            val elements = ArrayList<String>()
            elements.add("${Tab.USER}.login");elements.add("${Tab.USER}.password")
            elements.add("${Tab.USER}.email");elements.add("${Tab.USER}.phone");elements.add("${Tab.USER}.fullname");
            elements.add("${Tab.USER}.country");elements.add("${Tab.USER}.city");elements.add("${Tab.USER}.district");
            elements.add("${Tab.USER}.street");elements.add("${Tab.USER}.flat_number");elements.add("${Tab.USER}.post_code");
            dataToSend = elements.joinToString(separator = ", ")
            dataToSend = dataToSend.plus("|")
            dataToSend = dataToSend.plus("'${user.login}', '${user.password}', '${user.email}', '${user.phone}', '${user.fullname}', '${user.country}', '${user.city}', '${user.district}', '${user.street}', '${user.flatNumber}', '${user.postCode}'")
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }

    }

    fun deleteUser(context:Context, login: String){
        val funSend = "deleteUser"

        var dataToSend = "login = '${login}'"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    Toast.makeText(context, "User ${login} was deleted.", Toast.LENGTH_SHORT).show()
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

    fun addCompany(context: Context, adding: Boolean, company:CleaningCompany, nip: String = ""){

    }

    fun deleteCompany(context: Context, nip: String){
        val funSend = "deleteCompany"

        var dataToSend = "nip = '${nip}'"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    Toast.makeText(context, "Company ${nip} was deleted.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
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

    fun addVehicle(context: Context, adding: Boolean, vehicle: Vehicle, vehicleId: String = ""){

    }

    fun deleteVehicle(context: Context, vehicleId: String){
        val funSend = "deleteVehicle"

        var dataToSend = "id = '${vehicleId}'"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    Toast.makeText(context, "Vehicle ${vehicleId} was deleted.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
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

    fun addWorker(context: Context, adding: Boolean, worker: Worker, fullname: String = "", birthDate: String = ""){
        var funSend = "addCollectingPoint"
        if(adding) {
            funSend = "addWorker1"}
        else funSend = "updateWorker1"
        val elements = ArrayList<String>()
        elements.add("${Tab.WORKER}.fullname");elements.add("${Tab.WORKER}.birthdate")
        elements.add("${Tab.WORKER}.job_start_time");elements.add("${Tab.WORKER}.job_end_time")
        elements.add("${Tab.WORKER}.company_nip");elements.add("${Tab.WORKER}.vehicle_id")
        var dataToSend = elements.joinToString(separator = ", ")
        dataToSend = dataToSend.plus("|")
        dataToSend = dataToSend.plus("'${worker.fullname}', '${worker.birthDate}', '${worker.jobStartTime}', '${worker.jobEndTime}', '${worker.cleaningCompanyNIP}', '${worker.vehicleId}'")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }

                    Toast.makeText(context, "Action was done successfully!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }

            }
        }
    }

    fun deleteWorker(context: Context, fullname: String, birthDate: String){
        val funSend = "deleteWorker"

        var dataToSend = "fullname = '${fullname}'"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    Toast.makeText(context, "Worker ${fullname} was deleted.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun checkLogin(context: Context, username: String, password: String): Boolean {
        val funSend = "checkUserForLogin"
        var userExists = false
        var dataToSend = "'${username}', '${Encryption.decrypt(password)}'"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        userExists = false
                        return@withContext
                    }
                    if(json?.contains("1") == true){
                    context.startActivity(Intent(context, MainActivity::class.java))
                        (context as Activity).finish()
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }

        }
        return userExists
    }

    private fun addIconsToMap(context: Context, map: MapView, items: ArrayList<OverlayItem>, collectedItems: ArrayList<String>) {
        for (item in items) {
            item.setMarker(context.resources.getDrawable(R.drawable.red_marker_v2))
        }
        var overlay = ItemizedOverlayWithFocus<OverlayItem>(
            items,
            object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                    return if (collectedItems.indexOf(item.uid) == -1){
                        Toast.makeText(context, "Hold to collect", Toast.LENGTH_SHORT).show()
                        true
                    } else{
                        Toast.makeText(context, "Trash already collected", Toast.LENGTH_SHORT).show()
                        false
                    }

                }

                override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                    if (collectedItems.indexOf(item.uid) == -1){
                        item.setMarker(context.resources.getDrawable(R.drawable.green_marker_v2))
                        context?.let { collectTrash(it, item) };
                        collectedItems.add(item.uid)
                        Toast.makeText(context, "Item marked as collected", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "Trash already collected", Toast.LENGTH_SHORT)
                            .show()
                    }
                    return false
                }
            }, context
        )
//        overlay.setFocusItemsOnTap(true);
//        overlay.setMarkerBackgroundColor(Color.CYAN)
        map.overlays.add(overlay);
    }
    private fun addCollectedIconsToMap(context: Context, map: MapView, items: ArrayList<OverlayItem>) {
        for (item in items) {
            item.setMarker(context.resources.getDrawable(R.drawable.green_marker_v2))
        }
        var overlay = ItemizedOverlayWithFocus<OverlayItem>(
            items,
            object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                    return  false


                }

                override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {

                    return false
                }
            }, context
        )
//        overlay.setFocusItemsOnTap(true);
//        overlay.setMarkerBackgroundColor(Color.CYAN)
        map.overlays.add(overlay);
    }
}