package com.example.trashapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.OnItemClickListener
import com.example.trashapp.R
import com.example.trashapp.classes.Role

class RoleItemAdapter(private val mData: ArrayList<Role>?,
                      private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<RoleItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_role, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData?.get(position)
        holder.textView1.text = "${item!!.name}"

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    fun sortByNameAscending(context: Context) {
        mData?.sortWith(compareBy { it.name.uppercase() })
        notifyDataSetChanged()
    }

    fun sortByNameDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.name.uppercase() })
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView1: TextView

        init {
            textView1 = itemView.findViewById(R.id.textView1)
        }
    }
}