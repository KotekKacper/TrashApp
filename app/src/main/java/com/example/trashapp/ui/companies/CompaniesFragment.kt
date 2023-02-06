package com.example.trashapp.ui.companies

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trashapp.DBUtils
import com.example.trashapp.MainActivity
import com.example.trashapp.R
import com.example.trashapp.SortButtonCallback
import com.example.trashapp.databinding.FragmentCompaniesBinding
import com.example.trashapp.ui.collectingpoints.AddPointActivity

class CompaniesFragment : Fragment(), SortButtonCallback {

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

        (activity as MainActivity).let {
            it.sortButton.visibility = View.VISIBLE
            it.sortButton.setOnClickListener { onSortButtonClicked() }
        }

        val addButton = binding.root.findViewById<Button>(R.id.buttonCompaniesAdd)
        addButton.setOnClickListener {
            val intent = Intent(context, AddCompanyActivity::class.java)
            context?.startActivity(intent)
        }

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewCompanies)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getCompanies(it, recyclerView) }

        return root
    }

    override fun onResume(){
        super.onResume()

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewCompanies)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getCompanies(it, recyclerView) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSortButtonClicked() {
        val sortOptions = arrayOf("Email (A-Z)","Email (Z-A)",
            "NIP (ascending)", "NIP (descending)",
            "Phone number (ascending)", "Phone number (descending)")
        val builder = AlertDialog.Builder(context)
        val adapter = DBUtils.companiesAdapter
        builder.setTitle("Sort by")
            .setItems(sortOptions) { _, which ->
                when (which) {
                    0 -> adapter.sortByEmailAscending(context!!)
                    1 -> adapter.sortByEmailDescending(context!!)
                    2 -> adapter.sortByNIPAscending(context!!)
                    3 -> adapter.sortByNIPDescending(context!!)
                    4 -> adapter.sortByPhoneAscending(context!!)
                    5 -> adapter.sortByPhoneDescending(context!!)
                }
            }
            .show()
    }

}