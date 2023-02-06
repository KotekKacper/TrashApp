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
import com.example.trashapp.classes.CleaningCompany

class CompanyItemAdapter(private val mData: ArrayList<CleaningCompany>?,
                         private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<CompanyItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_company, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData?.get(position)
        holder.textView1.text = item!!.email
        holder.textView2.text = "NIP: " + item!!.NIP
        holder.textView3.text = "Phone number: " + item!!.phone

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    fun sortByEmailAscending(context: Context) {
        mData?.sortWith(compareBy { it.email.uppercase() })
        notifyDataSetChanged()
    }

    fun sortByEmailDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.email.uppercase() })
        notifyDataSetChanged()
    }

    fun sortByNIPAscending(context: Context) {
        mData?.sortWith(compareBy { it.NIP.toLong() })
        notifyDataSetChanged()
    }

    fun sortByNIPDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.NIP.toLong() })
        notifyDataSetChanged()
    }

    fun sortByPhoneAscending(context: Context) {
        mData?.sortWith(compareBy { it.phone.trimStart('+').toLong() })
        notifyDataSetChanged()
    }

    fun sortByPhoneDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.phone.trimStart('+').toLong() })
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