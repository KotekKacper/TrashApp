package com.example.trashapp.ui.groups

import android.content.Context
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
import com.example.trashapp.adapters.GroupItemAdapter
import com.example.trashapp.adapters.ReportItemAdapter
import com.example.trashapp.databinding.FragmentGroupsBinding
import com.example.trashapp.ui.reports.AddReportActivity

class GroupsFragment : Fragment() {

    private var _binding: FragmentGroupsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GroupsViewModel::class.java)

        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewGroups)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val addButton = binding.root.findViewById<Button>(R.id.buttonGroupsAdd)
        addButton.setOnClickListener {
            val intent = Intent(context, AddGroupActivity::class.java)
            context?.startActivity(intent)
        }

        context?.let { DBUtils.getGroups(it, recyclerView,
            activity!!.getSharedPreferences("credentials", Context.MODE_PRIVATE).getString("login", "")!!) }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}