package com.example.trashapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.ConvertResponse.convertAllUsers
import com.example.trashapp.ConvertResponse.convertCollectionPoints
import com.example.trashapp.ConvertResponse.convertCompanies
import com.example.trashapp.ConvertResponse.convertGroups
import com.example.trashapp.ConvertResponse.convertUserReports
import com.example.trashapp.ConvertResponse.convertVehicles
import com.example.trashapp.ConvertResponse.convertWorkers
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList


object DBUtils {

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8888/")
        .build()
    val service = retrofit.create(ServerApiService::class.java)
    val imgService = retrofit.create(ImageUploadApi::class.java)
    val imgDownService = retrofit.create(ImageDownloadApi::class.java)

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

    fun getReports(context: Context, recyclerView: RecyclerView, username: String) {
        val funSend = "getReports"

        var elements = ArrayList<String>()
        elements.add("${Tab.TRASH}.id");elements.add("${Tab.TRASH}.localization");elements.add("${Tab.TRASH}.creation_date");
        elements.add("${Tab.TRASH}.trash_size");elements.add("${Tab.TRASH}.collection_date");elements.add("${Tab.TRASH}.user_login_report")
        val dataToSend = elements.joinToString(separator = ", ").plus("|${username}")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                var reportsArray = arrayListOf<Trash>()
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    reportsArray = json?.let { convertUserReports(json.toString()) }!!
                }
                    for (report in reportsArray!!){
                        while (true){
                            val images = arrayListOf<Drawable>()
                            var imgNumber = 1
                            try{
                                val response = imgDownService.getImages(report.id!!, imgNumber.toString())
                                val imageBytes = response.execute().body()?.bytes()
                                val bitmap = imageBytes?.let { BitmapFactory.decodeByteArray(imageBytes, 0, it.size) }
                                report.images?.add(BitmapDrawable(context.resources, bitmap))
                                images.add(BitmapDrawable(context.resources, bitmap))
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Log.e("ServerSQL", e.toString())
                                }
                                break
                            }
                            imgNumber++
                        }
                    }
                withContext(Dispatchers.Main) {
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
                            intent.putExtra("collectedBy", "user")
                            intent.putExtra("collectedVal", reportsArray?.get(position)?.userLoginReport?.toString())
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

    fun getGroups(context: Context, recyclerView: RecyclerView, username: String){
        val funSend = "getGroups"

        val elements = ArrayList<String>()
        elements.add("${Tab.USER}.login");elements.add("${Tab.USER}.password")
        elements.add("${Tab.USER}.email");elements.add("${Tab.USER}.phone");
        elements.add("${Tab.USER}.fullname");elements.add("${Tab.USER}.country");
        elements.add("${Tab.USER}.city");elements.add("${Tab.USER}.street");
        elements.add("${Tab.USER}.post_code");elements.add("${Tab.ROLE}.role_name")

        val dataToSend = elements.joinToString(separator = ", ")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(username, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
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

    fun getVehicles(context: Context, recyclerView: RecyclerView){
        val funSend = "getVehicles"

        var elements = ArrayList<String>()
        elements.add("id");elements.add("in_use");elements.add("localization")
        elements.add("filling");
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

                    val vehiclesArray = json?.let { convertVehicles(json.toString()) }
                    val adapter = VehicleItemAdapter(vehiclesArray, object : OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(context, AddVehicleActivity::class.java)
                            intent.putExtra("id", vehiclesArray?.get(position)?.id)
                            intent.putExtra("inUse", vehiclesArray?.get(position)?.inUse)
                            intent.putExtra("filling", vehiclesArray?.get(position)?.filling)
                            intent.putExtra("latitude",
                                vehiclesArray?.get(position)?.localization?.split(",")?.get(0)
                            )
                            intent.putExtra("longitude",
                                vehiclesArray?.get(position)?.localization?.split(",")?.get(1)
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

    fun getWorkers(context: Context, recyclerView: RecyclerView){
        val funSend = "getWorkers"

        var elements = ArrayList<String>()
        elements.add("fullname");elements.add("birthdate");elements.add("job_start_time")
        elements.add("job_end_time");elements.add("company_nip");elements.add("vehicle_id");
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

                    val workersArray = json?.let { convertWorkers(json.toString()) }
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


    fun addTrash(context: Context, pos: GeoPoint, chosen_imgs : ArrayList<Uri>, size: String, user_login_report: String? = null, vehicle_id: Int? = null, user_login: String? = null, crew_id:Int? = null){
        val funSend = "addTrash"
        var dataToSend = "'${pos.toDoubleString()}', '${user_login_report}', '${TrashSize.valueOf(size.uppercase()).intValue}'"
        for(img in chosen_imgs) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val uri = RequestBody.create(MediaType.parse("text/plain"), "10")

                    val contentResolver = context.contentResolver
                    val imageStream = contentResolver.openInputStream(img)
                    val bitmap = BitmapFactory.decodeStream(imageStream)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                    val imageBytes = byteArrayOutputStream.toByteArray()
                    val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes)
                    val image = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

                    val response = imgService.uploadImage(uri, image)
                    response.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                // Handle success
                                Toast.makeText(context, "Image uploaded!", Toast.LENGTH_SHORT).show()
                            } else {
                                // Handle failure
                                Toast.makeText(context, "Image uploading failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // Handle failure
                            Toast.makeText(context, "Image uploading failed dramatically", Toast.LENGTH_SHORT).show()
                        }
                    })
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("ServerSQL", e.toString())
                    }
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())){
                        return@withContext
                    }
                    (context as Activity).finish()
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

    fun addReport(context: Context, adding: Boolean, trash: Trash, id: String = ""){
        val funSend : String
        if(adding) {
            funSend = "addReport"
        } else {
            funSend = "updateReport"
        }
        var dataToSend =
            "'${trash.userLoginReport}', '${trash.localization}', '${trash.creationDate}'" +
            "'${trash.trashSize}', '${trash.trashType}', '${trash.creationDate}'"//, '${trash.user/vehicle/crew}'" //
        dataToSend = dataToSend.plus("|${id}")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    (context as Activity).finish()
                    if (adding){
                        Toast.makeText(context, "Report added successfully!", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(context, "Report updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun addGroup(context: Context, adding: Boolean, group: Group, id: String = ""){
        var funSend: String
        if (adding) funSend = "addGroup"
        else  funSend = "updateGroup"

        val elements = ArrayList<String>()
        elements.add("${Tab.CLEAN_CREW}.crew_name");elements.add("${Tab.CLEAN_CREW}.meet_date")
        elements.add("${Tab.CLEAN_CREW}.meeting_localization")
        var dataToSend = elements.joinToString(separator = ", ")
        dataToSend = dataToSend.plus("\n")
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
                    Toast.makeText(context, "Group added successfully!", Toast.LENGTH_SHORT).show()
                    (context as Activity).finish()
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
                    (context as Activity).finish()
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
                    Toast.makeText(context, "Action was done successfully!", Toast.LENGTH_SHORT).show()
                    (context as Activity).finish()
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

    fun addCompany(context: Context, adding: Boolean, company: CleaningCompany, nip: String = ""){
        val funSend: String
        if (adding) funSend = "addCompany"
        else funSend = "updateCompany"

        var dataToSend = ""
        val elements = ArrayList<String>()
        elements.add("${Tab.CLEAN_COMPANY}.nip");elements.add("${Tab.CLEAN_COMPANY}.email")
        elements.add("${Tab.CLEAN_COMPANY}.phone");elements.add("${Tab.CLEAN_COMPANY}.country");
        elements.add("${Tab.CLEAN_COMPANY}.city");elements.add("${Tab.CLEAN_COMPANY}.district");
        elements.add("${Tab.CLEAN_COMPANY}.street");elements.add("${Tab.CLEAN_COMPANY}.house_number");
        elements.add("${Tab.CLEAN_COMPANY}.flat_number");elements.add("${Tab.CLEAN_COMPANY}.post_code");
        dataToSend = elements.joinToString(",")
        dataToSend = dataToSend.plus("|")
        dataToSend = dataToSend.plus("${company.NIP}`${company.email}`" +
                "${company.phone}`${company.country}`${company.city}`${company.district}`" +
                "${company.street}`${company.houseNumber}`${company.flatNumber}`${company.postCode}")
        if (!adding){
            dataToSend = dataToSend.plus("|${nip}")
        }
        Log.i("ServerSQL", dataToSend)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    (context as Activity).finish()
                    if (adding){
                        Toast.makeText(context, "Company added successfully!", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(context, "Company updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun addVehicle(context: Context, adding: Boolean, vehicle: Vehicle, vehicleId: String = ""){
        val funSend: String
        if (adding) funSend = "addVehicle"
        else funSend = "updateVehicle"

        var dataToSend = ""
        val elements = ArrayList<String>()
        elements.add("${Tab.VEHICLE}.in_use")
        elements.add("${Tab.VEHICLE}.localization");elements.add("${Tab.VEHICLE}.filling");
        dataToSend = elements.joinToString(",")
        dataToSend = dataToSend.plus("|")
        var inUse = 'F'
        if (vehicle.inUse) {inUse = 'T'}
        dataToSend = dataToSend.plus("${inUse}`" +
                "${vehicle.localization}`${vehicle.filling}")
        if (!adding){
            dataToSend = dataToSend.plus("|${vehicleId}")
        }
        Log.i("ServerSQL", dataToSend)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson(dataToSend, funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    (context as Activity).finish()
                    if (adding){
                        Toast.makeText(context, "Vehicle added successfully!", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(context, "Vehicle updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun addWorker(context: Context, adding: Boolean, worker: Worker, fullname: String = "", birthDate: String = ""){
        var funSend: String
        if(adding) {
            funSend = "addWorker"}
        else funSend = "updateWorker"
        val elements = ArrayList<String>()
        elements.add("${Tab.WORKER}.fullname");elements.add("${Tab.WORKER}.birthdate")
        elements.add("${Tab.WORKER}.job_start_time");elements.add("${Tab.WORKER}.job_end_time")
        elements.add("${Tab.WORKER}.company_nip");elements.add("${Tab.WORKER}.vehicle_id")
        var dataToSend = elements.joinToString(separator = ",")
        dataToSend = dataToSend.plus("|")
        dataToSend = dataToSend.plus("${worker.fullname}`${worker.birthDate}`" +
                "${worker.jobStartTime}`${worker.jobEndTime}`${worker.cleaningCompanyNIP}`" +
                "${worker.vehicleId}")
        if (!adding){
            dataToSend = dataToSend.plus("|${fullname}")
            dataToSend = dataToSend.plus("|${birthDate}")
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
                    (context as Activity).finish()
                    if (adding){
                        Toast.makeText(context, "Worker added successfully!", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(context, "Worker updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
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

    fun getActiveTrashCount(context: Context, binding: FragmentAccountBinding){
        val funSend = "callActiveTrash"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson("active", funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    binding.activeCount.text = json
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
    }

    fun getArchiveTrashCount(context: Context, binding: FragmentAccountBinding){
        val funSend = "callArchiveTrash"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getJson("archive", funSend)
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    Log.i("ServerSQL", json.toString())
                    if (checkForError(context, json.toString())) {
                        return@withContext
                    }
                    binding.archiveCount.text = json
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ServerSQL", e.toString())
                }
            }
        }
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