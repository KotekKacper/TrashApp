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
import com.example.trashapp.classes.User

class UserItemAdapter(private val mData: ArrayList<User>?,
                      private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<UserItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData?.get(position)
        holder.textView1.text = item!!.login + " (" + item!!.roles?.sorted()?.joinToString(",") + ")"

        holder.textView2.text = item.email

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    fun sortByLoginAscending(context: Context) {
        mData?.sortWith(compareBy { it.login.uppercase() })
        notifyDataSetChanged()
    }

    fun sortByLoginDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.login.uppercase() })
        notifyDataSetChanged()
    }

    fun sortByRolesAscending(context: Context) {
        mData?.sortWith(compareBy { it.roles?.sorted()?.joinToString(",")?.uppercase() })
        notifyDataSetChanged()
    }

    fun sortByRolesDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.roles?.sorted()?.joinToString(",")?.uppercase() })
        notifyDataSetChanged()
    }

    fun sortByEmailAscending(context: Context) {
        mData?.sortWith(compareBy { it.email.uppercase() })
        notifyDataSetChanged()
    }

    fun sortByEmailDescending(context: Context) {
        mData?.sortWith(compareByDescending { it.email.uppercase() })
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView1: TextView
        var textView2: TextView

        init {
            textView1 = itemView.findViewById(R.id.textView1)
            textView2 = itemView.findViewById(R.id.textView2)
        }
    }
}