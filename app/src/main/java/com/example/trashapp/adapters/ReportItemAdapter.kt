package com.example.trashapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.R
import com.example.trashapp.classes.Trash


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