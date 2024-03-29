package com.example.trashapp.ui.groups

import android.app.AlertDialog
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
import com.example.trashapp.MainActivity
import com.example.trashapp.R
import com.example.trashapp.SortButtonCallback
import com.example.trashapp.adapters.GroupItemAdapter
import com.example.trashapp.adapters.ReportItemAdapter
import com.example.trashapp.databinding.FragmentGroupsBinding
import com.example.trashapp.ui.reports.AddReportActivity

class GroupsFragment : Fragment(), SortButtonCallback {

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

        (activity as MainActivity).let {
            it.sortButton.visibility = View.VISIBLE
            it.sortButton.setOnClickListener { onSortButtonClicked() }
        }

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

    override fun onResume(){
        super.onResume()

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewGroups)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getGroups(it, recyclerView,
            activity!!.getSharedPreferences("credentials", Context.MODE_PRIVATE).getString("login", "")!!) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSortButtonClicked() {
        val sortOptions = arrayOf("ID (ascending)","ID (descending)",
            "Name (A-Z)","Name (Z-A)",
            "Latitude (0-90)", "Latitue (90-0)",
            "Longtitude (0-180)", "Longtitue (180-0)",
            "Meeting date (ascending)", "Meeting date (descending)")
        val builder = AlertDialog.Builder(context)
        val adapter = DBUtils.groupsAdapter
        builder.setTitle("Sort by")
            .setItems(sortOptions) { _, which ->
                when (which) {
                    0 -> adapter.sortByIDAscending(context!!)
                    1 -> adapter.sortByIDDescending(context!!)
                    2 -> adapter.sortByNameAscending(context!!)
                    3 -> adapter.sortByNameDescending(context!!)
                    4 -> adapter.sortByLatAscending(context!!)
                    5 -> adapter.sortByLatDescending(context!!)
                    6 -> adapter.sortByLonAscending(context!!)
                    7 -> adapter.sortByLonDescending(context!!)
                    8 -> adapter.sortByMeetDateAscending(context!!)
                    9 -> adapter.sortByMeetDateDescending(context!!)
                }
            }
            .show()
    }
}