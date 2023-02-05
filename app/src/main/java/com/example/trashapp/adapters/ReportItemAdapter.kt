package com.example.trashapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.trashapp.*
import com.example.trashapp.ConvertResponse.convertFromSize
import com.example.trashapp.classes.Trash
import kotlinx.coroutines.*
import retrofit2.Retrofit
import java.time.Duration


class ReportItemAdapter(private val mData: ArrayList<Trash>?,
                        private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ReportItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData?.get(position)

        // loading first image associated with the report
        CoroutineScope(Dispatchers.IO).launch {
        try {
                try{
                    delay((10+(5*position)).toLong())
                    val response = DBUtils.imgDownService.getImages(item?.id!!, "0")
                    val imageBytes = response.execute().body()?.bytes()
                    if (imageBytes!!.size > 1){
                        val bitmap = imageBytes.let { BitmapFactory.decodeByteArray(imageBytes, 0, it.size) }
                        withContext(Dispatchers.Main){
                            val image = BitmapDrawable(holder.itemView.context.resources, bitmap)
                            holder.imageView.setImageDrawable(image)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("ServerSQL-Image", e.toString())
                    }
                }
            } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("ServerSQL-Image", e.toString())
            }
        }
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        holder.textView1.text = item?.creationDate.toString().subSequence(0,10)

        val loc = item?.localization?.split(",")
        holder.textView2.text = "("+"%.3f".format(loc!![0].toDouble())+","+"%.3f".format(loc!![1].toDouble())+")"
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

        item?.collectionDate?.let { Log.i("Date", it) }

        if (item?.collectionDate == null || item?.collectionDate == "null"){
            holder.textView3.text = "Not collected"
            holder.textView3.setTextColor(Color.RED)
        }
        else{
            holder.textView3.text = "Collected"
            holder.textView3.setTextColor(Color.GREEN)
        }
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    fun sortByCreationDateAscending(context: Context) {
        mData?.sortWith(compareBy({ it.creationDate }))
        notifyDataSetChanged()
    }

    fun sortByCreationDateDescending(context: Context) {
        mData?.sortWith(compareByDescending({ it.creationDate }))
        notifyDataSetChanged()
    }

    fun sortByCollectionDateAscending(context: Context) {
        mData?.sortWith(compareBy({ it.collectionDate }))
        notifyDataSetChanged()
    }

    fun sortByCollectionDateDescending(context: Context) {
        mData?.sortWith(compareByDescending({ it.collectionDate }))
        notifyDataSetChanged()
    }

    fun sortBySizeAscending(context: Context) {
        mData?.sortWith(compareBy({ convertFromSize(it.trashSize?: "Big") }))
        notifyDataSetChanged()
    }

    fun sortBySizeDescending(context: Context) {
        mData?.sortWith(compareByDescending({ convertFromSize(it.trashSize?: "Big") }))
        notifyDataSetChanged()
    }

    fun sortByLatAscending(context: Context) {
        mData?.sortWith(compareBy({ it.localization.split(",")[0].toDouble() }))
        notifyDataSetChanged()
    }

    fun sortByLatDescending(context: Context) {
        mData?.sortWith(compareByDescending({ it.localization.split(",")[0].toDouble() }))
        notifyDataSetChanged()
    }

    fun sortByLonAscending(context: Context) {
        mData?.sortWith(compareBy({ it.localization.split(",")[1].toDouble() }))
        notifyDataSetChanged()
    }

    fun sortByLonDescending(context: Context) {
        mData?.sortWith(compareByDescending({ it.localization.split(",")[1].toDouble() }))
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var textView1: TextView
        var textView2: TextView
        var textView3: TextView

        init {
            imageView = itemView.findViewById(R.id.imageView)
            textView1 = itemView.findViewById(R.id.textView1)
            textView2 = itemView.findViewById(R.id.textView2)
            textView3 = itemView.findViewById(R.id.textView3)
        }
    }
}