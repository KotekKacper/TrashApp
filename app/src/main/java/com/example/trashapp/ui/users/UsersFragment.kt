package com.example.trashapp.ui.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.adapters.CollectingPointItemAdapter
import com.example.trashapp.adapters.UserItemAdapter
import com.example.trashapp.databinding.FragmentUsersBinding
import com.example.trashapp.ui.collectingpoints.AddPointActivity

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(UsersViewModel::class.java)

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val addButton = binding.root.findViewById<Button>(R.id.buttonUsersAdd)
        addButton.setOnClickListener {
            val intent = Intent(context, AddUserActivity::class.java)
            context?.startActivity(intent)
        }

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getUsers(it, recyclerView) }

        return root
    }

    override fun onResume(){
        super.onResume()

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getUsers(it, recyclerView) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}