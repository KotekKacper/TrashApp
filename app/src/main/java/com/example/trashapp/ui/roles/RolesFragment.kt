package com.example.trashapp.ui.roles

import android.app.AlertDialog
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
import com.example.trashapp.databinding.FragmentRolesBinding

class RolesFragment : Fragment(), SortButtonCallback {

    private var _binding: FragmentRolesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(RolesViewModel::class.java)

        _binding = FragmentRolesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as MainActivity).let {
            it.sortButton.visibility = View.VISIBLE
            it.sortButton.setOnClickListener { onSortButtonClicked() }
        }

        val addButton = binding.root.findViewById<Button>(R.id.buttonRolesAdd)
        addButton.setOnClickListener {
            val intent = Intent(context, AddRoleActivity::class.java)
            context?.startActivity(intent)
        }

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewRoles)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getRoles(it, recyclerView) }

        return root
    }

    override fun onResume(){
        super.onResume()

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewRoles)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getRoles(it, recyclerView) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onSortButtonClicked() {
        val sortOptions = arrayOf("Name (A-Z)","Name (Z-A)")
        val builder = AlertDialog.Builder(context)
        val adapter = DBUtils.rolesAdapter
        builder.setTitle("Sort by")
            .setItems(sortOptions) { _, which ->
                when (which) {
                    0 -> adapter.sortByNameAscending(context!!)
                    1 -> adapter.sortByNameDescending(context!!)
                }
            }
            .show()
    }
}