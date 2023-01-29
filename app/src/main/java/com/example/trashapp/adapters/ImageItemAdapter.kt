package com.example.trashapp.adapters

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.*
import com.example.trashapp.classes.Group
import kotlinx.coroutines.*
import retrofit2.Retrofit

class ImageItemAdapter(private val mData: ArrayList<String>?
) :
RecyclerView.Adapter<ImageItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData?.get(position)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                try{
                    delay((10+(5*position)).toLong())
                    val response = DBUtils.imgDownByIdService.getImages(item.toString())
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

        holder.button.setOnClickListener {
            //TODO - implement deletion of an image
        }

    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var button: Button

        init {
            imageView = itemView.findViewById(R.id.imageView)
            button = itemView.findViewById(R.id.button)
        }
    }
}