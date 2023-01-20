package com.example.trashapp.ui.companies

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.DBUtils
import com.example.trashapp.R
import com.example.trashapp.adapters.CollectingPointItemAdapter
import com.example.trashapp.adapters.CompanyItemAdapter
import com.example.trashapp.databinding.FragmentCompaniesBinding

class CompaniesFragment : Fragment() {

    private var _binding: FragmentCompaniesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(CompaniesViewModel::class.java)

        _binding = FragmentCompaniesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewCompanies)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val companiesArray = context?.let { DBUtils.getCompanies(it) }
        val adapter = CompanyItemAdapter(companiesArray)
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}