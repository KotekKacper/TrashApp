package com.example.trashapp.adapters

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
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.*
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



//        if (item!!.images != null){
//            if (item.images?.isNotEmpty()!!){
//                holder.imageView.setImageDrawable(item.images!![0])
//            }
//        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        holder.textView1.text = item?.creationDate.toString().subSequence(0,10)

        val loc = item?.localization?.split(",")
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

        if (item?.collectionDate == null){
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