package com.example.trashapp.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.adapters.ReportItemAdapter
import com.example.trashapp.databinding.FragmentReportsBinding

class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(ReportsViewModel::class.java)

        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewReports)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val reportsArray = context?.let { DBUtils.getReports(it, "admin") }
        val adapter = ReportItemAdapter(reportsArray)
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}