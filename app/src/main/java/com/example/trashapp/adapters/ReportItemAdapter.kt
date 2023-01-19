package com.example.trashapp.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.R
import com.example.trashapp.classes.Trash
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class ReportItemAdapter(private val mData: ArrayList<Trash>?) :
    RecyclerView.Adapter<ReportItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData?.get(position)
        if (item!!.images != null){
            holder.imageView.setImageDrawable(item.images!![0])
        }
        holder.textView1.text = item.creationDate.toString().subSequence(0,10)
        Log.i("Localization", item.localization)
//        val loc = item.localization.split(",")
//        val url = URL("https://nominatim.openstreetmap.org/reverse?lat=${loc[0]}&lon=${loc[1]}&format=json")
//        val urlConn = url.openConnection()
//        val bufferedReader = BufferedReader(InputStreamReader(urlConn.getInputStream()))
//
//        val stringBuffer = StringBuffer()
//        var line: String?
//        while (bufferedReader.readLine().also { line = it } != null) {
//            stringBuffer.append(line)
//        }
//        val locJson = JSONObject(stringBuffer.toString())
        holder.textView2.text = item.localization // TODO - replace with the result from nominatim API
        if (item.collectionDate == null){
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