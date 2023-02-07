package com.example.trashapp.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.LocalizationToAddress
import com.example.trashapp.NominatimApiService
import com.example.trashapp.OnItemClickListener
import com.example.trashapp.R
import com.example.trashapp.classes.Group
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class GroupItemAdapter(private val mData: ArrayList<Group>?,
                       private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<GroupItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData?.get(position)
        holder.textView1.text = item!!.name + " (ID: ${item.id})"

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        val loc = item.meetingLoc?.split(",")
        if ((loc?.get(0) ?: "") != "" && (loc?.get(1) ?: "") != ""){
            holder.textView2.text = "("+"%.3f".format(loc?.get(0)!!.toDouble())+","+"%.3f".format(loc[1].toDouble())+")"
        } else{
            holder.textView2.text = "No meeting address"
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .build()
        val service = retrofit.create(NominatimApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getReverseJson(loc!![0], loc[1], "json")
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    val addrString = LocalizationToAddress.getAddress(json)
                    holder.textView2.text = addrString
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // handle error
                    Log.e("Localization", e.toString())
                }
            }
        }

        if (item.meetingDate != null){
            holder.textView3.text = item.meetingDate.toString().subSequence(0, 10)
        } else {
            holder.textView3.text = "Date not specified"
        }

    }

    fun sortByNameAscending(context: Context) {
        mData?.sortWith(compareBy { it.name.uppercase() })
        notifyDataSetChanged()
    }

    fun sortByNameDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.name.uppercase() })
        notifyDataSetChanged()
    }

    fun sortByLatAscending(context: Context) {
        try{
            mData?.sortWith(compareBy {
                if (it.meetingLoc!!.split(",")[0].isEmpty()) {
                    0.0
                } else {
                    it.meetingLoc.split(",")[0].toDouble()
                }
            })
            notifyDataSetChanged()
        } catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    fun sortByLatDescending(context: Context) {
        try{
            mData?.sortWith(compareByDescending {
                if (it.meetingLoc!!.split(",")[0].isEmpty()) {
                    0.0
                } else {
                    it.meetingLoc.split(",")[0].toDouble()
                }
            })
            notifyDataSetChanged()
        } catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    fun sortByLonAscending(context: Context) {
        try{
            mData?.sortWith(compareBy {
                if (it.meetingLoc!!.split(",")[1].isEmpty()) {
                    0.0
                } else {
                    it.meetingLoc.split(",")[1].toDouble()
                }
            })
            notifyDataSetChanged()
        } catch (ex: Exception){
        ex.printStackTrace()
        }
    }

    fun sortByLonDescending(context: Context) {
        try{
            mData?.sortWith(compareByDescending {
                if (it.meetingLoc!!.split(",")[1].isEmpty()) {
                    0.0
                } else {
                    it.meetingLoc.split(",")[1].toDouble()
                }
            })
            notifyDataSetChanged()
        } catch (ex: Exception){
        ex.printStackTrace()
        }
    }

    fun sortByMeetDateAscending(context: Context) {
        mData?.sortWith(compareBy { it.meetingDate })
        notifyDataSetChanged()
    }

    fun sortByMeetDateDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.meetingDate })
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView1: TextView
        var textView2: TextView
        var textView3: TextView

        init {
            textView1 = itemView.findViewById(R.id.textView1)
            textView2 = itemView.findViewById(R.id.textView2)
            textView3 = itemView.findViewById(R.id.textView3)
        }
    }
}