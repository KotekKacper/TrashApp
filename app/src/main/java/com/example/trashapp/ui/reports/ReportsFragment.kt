package com.example.trashapp.ui.reports

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.DBUtils
import com.example.trashapp.MainActivity
import com.example.trashapp.R
import com.example.trashapp.SortButtonCallback
import com.example.trashapp.adapters.ReportItemAdapter
import com.example.trashapp.databinding.FragmentReportsBinding

class ReportsFragment : Fragment(), SortButtonCallback {

    private var _binding: FragmentReportsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: ReportItemAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(ReportsViewModel::class.java)

        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as MainActivity).let {
            it.sortButton.visibility = View.VISIBLE
            it.sortButton.setOnClickListener { onSortButtonClicked() }
        }

        recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewReports)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val addButton = binding.root.findViewById<Button>(R.id.buttonReportsAdd)
        addButton.setOnClickListener {
            val intent = Intent(context, AddReportActivity::class.java)
            context?.startActivity(intent)
        }

        context?.let { DBUtils.getReports(it, recyclerView,
            activity!!.getSharedPreferences("credentials", Context.MODE_PRIVATE).getString("login", "")!!) }

        return root
    }

    override fun onResume() {
        super.onResume()
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewReports)
        recyclerView.layoutManager = LinearLayoutManager(context)

        context?.let { DBUtils.getReports(it, recyclerView,
            activity!!.getSharedPreferences("credentials", Context.MODE_PRIVATE).getString("login", "")!!) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSortButtonClicked() {
        val sortOptions = arrayOf("Creation date (oldest-newest)", "Creation date (newest-oldest)")
        val builder = AlertDialog.Builder(context)
        val adapter = DBUtils.reportsAdapter
        builder.setTitle("Sort by")
            .setItems(sortOptions) { _, which ->
                when (which) {
                    0 -> adapter.sortByCreationDateAscending(context!!)
                    1 -> adapter.sortByCreationDateDescending(context!!)
                }
            }
            .show()
    }
}