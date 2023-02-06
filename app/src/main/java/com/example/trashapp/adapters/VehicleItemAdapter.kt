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
import com.example.trashapp.OnItemClickListener
import com.example.trashapp.R
import com.example.trashapp.classes.Vehicle

class VehicleItemAdapter(private val mData: ArrayList<Vehicle>?,
                      private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<VehicleItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehicle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData?.get(position)
        holder.textView1.text = item!!.id

        if (item.localization?.contains(",") == true){
            val loc = item.localization!!.split(",")
            if (loc[0].isNotEmpty() && loc[1].isNotEmpty()){
                holder.textView2.text = "("+"%.3f".format(loc[0].toDouble())+
                        ","+"%.3f".format(loc[1].toDouble())+")"
            } else {
                holder.textView2.text = "Localization unknown"
            }
        } else {
            holder.textView2.text = "Localization unknown"
        }

            holder.textView3.text = (item.filling*100).toString() + "%"

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    fun sortByIDAscending(context: Context) {
        mData?.sortWith(compareBy { it.id.toLong() })
        notifyDataSetChanged()
    }

    fun sortByIDDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.id.toLong() })
        notifyDataSetChanged()
    }

    fun sortByLatAscending(context: Context) {
        try{
            mData?.sortWith(compareBy {
                if (it.localization!!.split(",")[0].isEmpty()) {
                    0.0
                } else {
                    it.localization!!.split(",")[0].toDouble()
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
                if (it.localization!!.split(",")[0].isEmpty()) {
                    0.0
                } else {
                    it.localization!!.split(",")[0].toDouble()
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
                if (it.localization!!.split(",")[1].isEmpty()) {
                    0.0
                } else {
                    it.localization!!.split(",")[1].toDouble()
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
                if (it.localization!!.split(",")[1].isEmpty()) {
                    0.0
                } else {
                    it.localization!!.split(",")[1].toDouble()
                }
            })
            notifyDataSetChanged()
        } catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    fun sortByFillingAscending(context: Context) {
        mData?.sortWith(compareBy { it.filling })
        notifyDataSetChanged()
    }

    fun sortByFillingDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.filling })
        notifyDataSetChanged()
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