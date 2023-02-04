package com.example.trashapp.adapters

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
import com.example.trashapp.classes.TrashCollectingPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class CollectingPointItemAdapter(private val mData: ArrayList<TrashCollectingPoint>?,
                                 private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<CollectingPointItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_collecting_point, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData?.get(position)

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        val loc = item!!.localization.split(",")
        holder.textView1.text = "("+"%.3f".format(loc!![0].toDouble())+","+"%.3f".format(loc!![1].toDouble())+")"
        val retrofit = Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .build()
        val service = retrofit.create(NominatimApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getReverseJson(loc[0], loc[1], "json")
                withContext(Dispatchers.Main) {
                    val json = response.body()?.string()
                    val addrString = LocalizationToAddress.getAddress(json)
                    holder.textView1.text = addrString
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // handle error
                    Log.e("Localization", e.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView1: TextView

        init {
            textView1 = itemView.findViewById(R.id.textView1)
        }
    }
}