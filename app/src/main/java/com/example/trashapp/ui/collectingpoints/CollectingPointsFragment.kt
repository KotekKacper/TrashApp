package com.example.trashapp.ui.collectingpoints

import android.app.AlertDialog
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
import com.example.trashapp.adapters.CollectingPointItemAdapter
import com.example.trashapp.adapters.GroupItemAdapter
import com.example.trashapp.databinding.FragmentCollectingPointsBinding
import com.example.trashapp.ui.groups.AddGroupActivity

class CollectingPointsFragment : Fragment(), SortButtonCallback {

    private var _binding: FragmentCollectingPointsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(CollectingPointsViewModel::class.java)

        _binding = FragmentCollectingPointsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as MainActivity).let {
            it.sortButton.visibility = View.VISIBLE
            it.sortButton.setOnClickListener { onSortButtonClicked() }
        }

        val addButton = binding.root.findViewById<Button>(R.id.buttonCollectingPointsAdd)
        addButton.setOnClickListener {
            val intent = Intent(context, AddPointActivity::class.java)
            context?.startActivity(intent)
        }

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewCollectingPoints)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getCollectingPoints(it, recyclerView) }

        return root
    }

    override fun onResume(){
        super.onResume()

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewCollectingPoints)
        recyclerView.layoutManager = LinearLayoutManager(context)
        context?.let { DBUtils.getCollectingPoints(it, recyclerView) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSortButtonClicked() {
        val sortOptions = arrayOf(
            "Latitude (0-90)", "Latitue (90-0)",
            "Longtitude (0-180)", "Longtitue (180-0)")
        val builder = AlertDialog.Builder(context)
        val adapter = DBUtils.pointsAdapter
        builder.setTitle("Sort by")
            .setItems(sortOptions) { _, which ->
                when (which) {
                    0 -> adapter.sortByLatAscending(context!!)
                    1 -> adapter.sortByLatDescending(context!!)
                    2 -> adapter.sortByLonAscending(context!!)
                    3 -> adapter.sortByLonDescending(context!!)
                }
            }
            .show()
    }
}